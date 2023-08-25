package com.inn.cafe.service.seriveImpl;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.CustomerUserDetailsService;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.POJO.User;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.utils.EmailUtils;
import com.inn.cafe.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j//This is used for the Login purpose..
@Service//Here we are actually gonna write the actual business logic.
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {//Here first we need to validate the requestMap where values like email, mobile are to be validated/restricted.

        log.info("Inside Signup {}", requestMap);//We are going to print the log (requestMap). And the log we print inside {}. //So Whenever we hit the API, first it will log all the data which is available in the requestMap after that it will check for the stuff mentioned in the below method. So that we can easily rectify the issue with requestMap data. Like if we want to keep track of the data or keep track of the debug the code/issue.
        //So the next thing we need to use the validateSignUpMap() method in if block to validate the data.

        //So to handle the exceptions we put the whole code into try catch block, and handle exceptions in the catch block.
        try {
            if (validateSignUpMap(requestMap)) {//Here we r using the method and to that we r supplying requestMap to check the parameters.
                //We have to check that user/email/contact exist in the database or no, if no then only we should proceed with this block. So to check that we need to use DAO/Repository layer and build a custom query there.
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {//If user id not exist in DB already then this if block runs
                    userDao.save(getUserFromMap(requestMap));//But here we cannot pass the requestMap data directly to the save method. We need a User object in order to save the data into DB. So in this case we will create another method that takes all data and puts into User object and then we can save the object into DB.
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists..", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);//Now our API is ready so we test the API to create a user into DB.
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")) {
            return true;
        }
        return false;//It automatically acts as else part.
    }

    //To extract the data from the requestMap and set it in the User object and return that.
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));//We have to use the same name as used in the above method validateSignUpMap.
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    //We are building API here onwards.
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");//here we r not writing requestMap bcz it is like printing the username and password in the console.
        try {
            //Inside this firstly we authenticate the user.
            //We need to use Authentication so creating a bean of AuthenticationManager
            Authentication auth = authenticationManager.authenticate(//We need to import the Authentication from the package "org.springframework.security.core.Authentication"
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {//We are creating CustomerUserDetailsService bean.. //By this we are checking if user is actually authenticated or not? and as well as Admin has approved or not.. //If equalsIgnoreCase = true mean admin has approved the user.
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);//We haven't created the object of JwtUtils so we create its bean here.
                }//If user is not approved then what we have to do is given in else.
                else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for Admin Approval!.." + "\"}", HttpStatus.BAD_REQUEST);//We are showing the message to wait for approval.
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials!.." + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {//We r checking user is admin or not?
                //Then we need to check if the user exist or not?
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {//We can also write like if
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status is Updated Successfully!..", HttpStatus.OK);
                } else {
                    CafeUtils.getResponseEntity("User Id doesn't exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());//We are removing the current admin email from the email we are sending.
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account is Approved!..", "USER: " + user + "\n is approved by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account is Disabled!..", "USER: " + user + "\n is Disabled by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (userObj != null) {//This condition can be also written as (userObj != null)
                //Now we got the email of user, now we have to check for old password and then move forward to changing the password
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);//userObj contains all the data including the new password.
                    return CafeUtils.getResponseEntity("Password Updated Successfully!..", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password!..", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {//We can also write the condition as "user != null"
                emailUtils.forgotPasswordMail(user.getEmail(), "Credentials by Cafe-Management System..", user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check your mail for Credentials...", HttpStatus.OK);//We are showing this message for every email entered by User in the form of JSON bcz anyone can check which user email is registered & which user email is not registered.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}