package com.blog.repositories;

import com.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {//In repository layer findByUsername or findByCity or findByEmail such built-in methods are not present in JPA Repository.

    Optional<User> findByEmail(String email);//Just by using method starting with findBy, followed by column name(email) & we will use camelCasing here. With this it'll automatically searches for the record when we give Email id to it. And this will return back Optional class with return type User. //When we write UserRepository.findByEmail when we call this method, Spring boot is very intelligent enough to understand the meaning of this method, & it'll automatically write the SQL query and get the record for that email-id. So this is equivalent to writing 'SELECT * FROM User WHERE email='"+email+"''.
    Optional<User> findByUsernameOrEmail(String username, String email);//Inorder to use findByEmail or findByUsername both we use this method.
    Optional<User> findByUsername(String username);//So to perform this action the SQL query would become (select * from user where email="" or username=""). //So we cannot write SQL query here, bcz SQL query will be written down by Hibernate internally. //So this will give the record if email-id matches or the username matches or both r present. Either one or both. When it'll be false?->Either email or username mismatches or false.
    //The method we write should be meaningful, and it can be even searchByEmail, getByEmail. Just we should give it a meaningful name that's it.

    //Now whenever we register a user or signing up, we want to firstly check in the DB does the user exist or the email exist, if yes we should not register the same user or the email again or we should not be allowed to signup with same credentials again. For this we use the below 2 methods.
    Boolean existsByUsername(String username);//We r using Wrapper class Boolean here. Not the primitive DT boolean.
    Boolean existsByEmail(String email);

    //We can have some scenarios like: We are developing a feature, Search bar for Travel Website. In that we have one of the feature - places to visit, We should create a feature there, if we put Bangalore there, it should recommend all the places to visit in Bangalore. So in such case we require a method findByCity() or findByPlace() to be developed.
}