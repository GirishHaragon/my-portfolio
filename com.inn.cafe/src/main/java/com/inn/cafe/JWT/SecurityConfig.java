package com.inn.cafe.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity//This uses Spring Security dependency.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;//It requires a class to be created so we create a class.

    @Autowired
    JwtFilter jwtFilter;

    //We used the Alt+Insert the Override methods then chose auth:AuthenticationBuilder(1st one)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Inside this configure it actually expect one customer user details service class & bean of that.
        auth.userDetailsService(customerUserDetailsService);
    }

    //We will use password encoder here.. By creating the bean of PasswordEncoder.
    @Bean//If we don't apply this annotation it will throw error during runtime/execution.
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();//This is not recommended but for learning we r following now.. In further we will encrypt the password.
    }
    //Next we need authentication manager bean also,, So we use Alt+Insert then override AuthenticationManagerBean() method.
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //We again have to override another method, so Alt+Insert. Then in that we need configure(HttpSecurity) method.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Firstly we r disabling this HttpSecurity cors stuff. And CSRF we need to disable for now..
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf().disable()//Cross Site Request Forgery
                .authorizeRequests()//We should not miss this step or else we don't get antMatchers method.
                .antMatchers("/user/login", "/user/signup", "/user/forgotPassword")//And we r going to bypass the Signup, login & forgot password APIs. Without using JWT token we can access these mentioned urls by specifying here.
                .permitAll()//It helps to permit all the urls mentioned in the antMatchers.
                .anyRequest()//It helps to allow any requests with the permitted urls
                .authenticated()//Authenticating users with the urls mentioned in antMatcher
                .and().exceptionHandling()//Also handling exceptions occurring in the above urls.
                .and()
                .sessionManagement()//Also managing the sessions in the Urls.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//We need to study these by ourselves by online reference.
        //After this we have to add the filter the token and other stuffs.
        //For that We have to create one more class that is of JWT filter type.
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);//We are now writing double quotes as string filter type then & resolving the error on filter type. So we are creating a bean of JwtFilter class. And simply passing the jwtFilter object.

    }
}
