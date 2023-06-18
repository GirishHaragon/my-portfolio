package com.blog.config;

import com.blog.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration//On applying this anno SB will refer this file configuration, and understand what it needs to do,, //With this class we can see the change in the sign in page/form on web page after pasting the link. //All our configuration stuff we can develop inside this class using @Configuration anno.
@EnableWebSecurity //We use this anno for telling that i don't want to go with default web security concept, & enable this class or use this class for the spring security configuration..  //This code we all get it online, so no need to by-heart code.
@EnableGlobalMethodSecurity(prePostEnabled = true)//This one is updated.
public class SecurityConfig extends WebSecurityConfigurerAdapter {//Here the 'WebSecurityConfigurerAdapter' is a Deprecated API Usage.

    @Autowired//These 2 lines are updated ones.
    private CustomUserDetailsService userDetailsService;//We have just created a bean for CustomUserDetailsService. Before this update we have already configured the class.

    //To build the password encoding technique, firstly we build a method here,,
    @Bean//The Bean to be generated, we should ensure that @Bean anno is used, because whatever object creation we r doing in the method is to be supplied automatically to the place where required/used. BCryptpasswordEncoder is an External library, therefore @Autowired will not able to create right object itself. Bcs Spring IOC doesn't have knowledge of this particular object. Bcs IOC is the one, that does DependencyInjection when we use @Autowired. So this class it doesn't have.. So @Bean takes care of the thing.
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//This means when we call this method, it is returning the Object 'BCryptPasswordEncoder()'. What is present inside this object is the Encoding method we needed.
    }//Password encoder was also there earlier.

    @Override//We have created this Bean creation method for AuthenticationManager to solve the error occurring after developing signin feature and running the application.
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override//This annotation is written by the updated code.
    protected void configure(HttpSecurity http) throws Exception {//First, we were noticing that the username & password were being generated automatically, so it was the default settings, provided by the jar (spring security). So by this we are commanding spring boot that it should not go with default credentials, instead we are commanding sb that we ourself configure credentials & it has to follow.
        http   //By adding this method in this class, what we r commanding is, anyRequest() that we enter in the URL, that request firstly it converts all of that with httpBasic() authentication.
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll() //We are adding this extra line here. We are specifying 'HttpMethod.GET' which means wherever we used getMapping in our project '/api/**', now that can be '/api/post or comments' anything that happens is starts with api & after that it can be anything. As long as we see this kind of url we permit all.. //Which means everybody can access, there is no Authentication required. But any other thing we do, then it should be authenticated. That means what r the other things, like PutMapping, PostMapping, DeleteMapping, these are only secured. But GetMapping we r keeping it open by applying this line. //By this line we r getting all posts openly in PostMan with Getmapping. //Like if we want to keep the login page/signin/signup page open we can do like this with the help of this line, without authorising everybody can access these pages.
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }//Now to test we require Username & Password, that's where we go with In-memory Authentication.

    @Override//These 5 extra lines r updated ones. So we need to use the AuthenticationBuilder class auth. "auth.userDetailsService(userDetailsService)"
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)//And to this we supply userDetailsService object.
                .passwordEncoder(passwordEncoder());//And the passwordEncoder. These r the extra steps.
    }

    //Now the next thing, we will apply a user detail service method, which will have 2 objects, this is where the In-Memory authentication is happening. We r not saving the name & passwords in the DB, but we r creating here an object, in that object is what we r storing the user details, for now..
    //Now we are updating this file with the copied code and we are not using the below In-Memory Authentication so i have commented it..
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService(){
//        UserDetails ramesh = User.builder().username("pankaj").password(passwordEncoder() //Here User is a built in class from security package of SpringFramework, now this User class has a builder method.In User.builder() we give user as admin. And the password we give needs to be Encoded one. We should not store the password as it is in the DB bcs if anybody gets the db access even developers, all the passwords are exposed. So Encoding technique is like if we give password as 'password' it will convert that into some Encoded vale (16-digit more or less). To see how this password Encoding happens we create a 'Main class' in util package. And test is.
//                .encode("password")).roles("USER").build(); //Once Object creation is done properly. The first person here is ramesh whose username is pankaj & role will be a user, which means User cannot save, update & delete the Post.
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder() //Here with help of ".password(passwordEncoder()" we r calling the method. Here 'this' keyword is automatically working to call the method. That's where 'this' keyword is very popular here. //So 'passwordEncoder()' will call password encoder method created above, which has the 'BCryptPasswordEncoder()' object in it & it returns the same obj, that object has 'encode()' & the password will be encoded. Because without encoding passwords SpringSecurity will not work. That is a rule, if we r using SpringSecurity, encoding is mandatory, we can disable Encoding but that is not recommended/not a good practice. Which means we r reducing security.
//                .encode("password")).roles("ADMIN").build(); //So there is another user we created here is, admin. Username is admin and also password is admin. And his role is gonna be Admin. So only an Admin can create the Post, delete & update the Post.  //So to configure the Admin things we use "@PreAuthorize" over the methods in controller layer, which are PostMapping, DeleteMapping, UpdateMapping. But not GetMapping.
//        return new InMemoryUserDetailsManager(ramesh, admin);
//    }
}