package com.blog.controller;

import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.payload.JWTAuthResponse;
import com.blog.payload.LoginDto;//I have removed springboot from the import statement.
import com.blog.payload.SignUpDto;
import com.blog.repositories.RoleRepository;
import com.blog.repositories.UserRepository;//Even after commenting this i m not getting any errors, bcz it has not been used anywhere
import com.blog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;//These 3 @Autowired Bean creations (UserRepository, RoleRepository & PasswordEncoder) are developed for the sake of signup feature.

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; //We require this passwordEncoder Bean, automatically @Bean we have done in Config file. That will automatically inject the address into it.

    @Autowired
    private JwtTokenProvider tokenProvider;//We have added this bean of tokenProvider while applying JWT concept to our project.


    //http://localhost:8080/api/auth/signin  //We will supply the JSON object to this URL which will get copied into the
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){//While applying JWT , we modified this ResponseEntity<String to JWTAuthResponse>.
        Authentication authentication = authenticationManager.authenticate( //Now there is a Built-in feature which is called authenticationManager.authenticate we have to just apply. And in that we create an object of UsernamePasswordAuthenticationToken. Which the name itself says it will take the username & password and it will generate a token only if credentials are valid. And if it is not valid the it will just go ahead and fail the authentication. which means it will take care of everything if we just supply the username & password to it. Then it will just validate if credentials are valid it will generate a token. If not valid it will fail.
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );//If it fails to generate token, the program will halt here only, and if it is successful then further it will send the token and display the below message to the user. If not program will crash.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);  //Since we are applying JWT, we are removing this line. And modifying

        //After this we add some more below 2 lines. //get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));  //So now we need to create this JWTAuthResponse class in Payload package, which will send the response back, and the response will be, once we sign in, this JWTAuthResponse will send back Token to the response section of our Postman or browser.//Obviously right, if you sign in or if it is a valid username & password, JWTAuthResponse will return back a Token to Postman/Browser then that token we can use it for all subsequent transactions/activities.
    }

    //http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){//So Firstly the content here is going to SignUpDto which is not developed yet, so now we will build the SignUpDto class in Payload package, which should contain the fields of Registration contents. So after we develop SignUpDto, from the JSON the content will go to SignUpDto. //So what we are doing here is for Sign up feature, first Step,  we will call this method  to which JSON content is given to this object. SignUpDto will take the Content of JSON & it will put that into it.

        // add check for username exists in a DB
        if(userRepository.existsByUsername(signUpDto.getUsername())){ //Now in this it requires the userRepository to check if any user is already registered or not. It will mainly check here, is there is Username/email already used/taken, if exists true then do not sign up, just display the message Username & Email already taken.
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);//If this method gets true then execution returns back here only returning the message "Username is already taken".
        }//Now Second Step :- We check does the username already exist in DB or not? if yes it return true and reports the message in Postman/FrontEnd. If not, it returns false. And signup continues. The return type in the method(registerUser) is generic (<?>), why generic bcz if we make it generic the return type can be anything. Which means return type is not fixed. Return type can be String or it can also be other than String. Or it can be a dto. If the return type is only dto or only String then don't make it generic make it appropriate one. And if the return type would be String or Dto then make it Generic. How do we make it a generic?-> put a ? in the angular braces or Diamond bracket.

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){ //In repository layer we have added methods, that returns Boolean values.
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);//Now Second Step :- We check does the email already exist in DB or not? if yes it return true. If not it returns false.
        }//If both of the if blocks happen to be false then we create an entity object (User) and we copy the content of dto (signUpDto), then using passwordEncoder.encode we encode the password.

        // create User Entity object
        User user = new User();//If Username & emails doesn't exist then we copy the data from the Dto to User, and Save it.
        user.setName(signUpDto.getName());//Here we are copying the dto contents to entity using getters & setters.
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));//Here we are encoding the password given by the user or present in the SignUpDto and saving it in the entity.

        Role roles = roleRepository.findByName("ROLE_ADMIN").get(); //Now whoever signsUp will by-default he gets the role as ADMIN. That's what we are doing here. Using roelRepository.findByName, we are finding the role by name, when we apply get on it, it will convert that into Role object.
        user.setRoles(Collections.singleton(roles)); //We apply this because the role object needs to get converted into role whether it is Admin or User.

        userRepository.save(user);//Then after all this we save the user details.

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    } //If the requirement is like only admin can create User not anybody can, then we don't create user object. Actually whoever is signing up we r making them admin. But usually for Admin usage we can Hardcode the value in DB. And then let admin create users. To allow only admin can create user then we develop Admin to login first then he can have the feature of creating a new user.
}//Or basically we could have also done like, we could have given one more variable in the JSON object, called as Role. Rather than making it complex (By-default User becomes admin). Based on the role get the role object, roleRepository.findByName() so that we have given the names like ADMIN and USER. It will get that role and save it, Job done..