package com.inn.cafe.JWT;


import com.inn.cafe.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j//This is a lombok annotation.
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.inn.cafe.POJO.User userDetail;//We are specifying this class path bcz the User class is already present/available in Spring security package. So that's the reason we need to specify the path. Or we can also import the user from POJO layer carefully both are same.

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userDao.findByEmailId(username);//So we are actually supplying email here as variable username. from User entity.
        if (!Objects.isNull(userDetail)) {//We r checking if the userDetails containing username/email & password is exist in the DB. If we user exist the email then complete userDetail data should be fetched.
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not Found.");
        }
    }

    //We r going to create another method that will return complete userDetail if we need in any case.
    public com.inn.cafe.POJO.User getUserDetail(){
//        com.inn.cafe.POJO.User user = userDetail;
//        user.setPassword(null);//Here we are actually making the password as null. Bcz we don't expose password. So we are showing as null.
//        return user;//Its simply returning all the userDetail containing user data.
        //Either we can do this or we can simply pass all the data, and expose the password also
        return userDetail;//This is returning all the data exposing the password.
    }
}
