package com.inn.cafe.dao;

import com.inn.cafe.POJO.User;
import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email") String email);//This method utilizes the provided query in the POJO (@NamedQuery) and finds the email whether present/not in DB.
    //We need an implementation of the method where we can make use of the email supplied here and for that check we are writing an annotation of in Entity class User as @NamedQuery, in which we check the email by writing the query ourselves.

    List<UserWrapper> getAllUser();//For this to work we write another @NamedQuery in the POJO class.

    List<String> getAllAdmin();

    //When ever we write a query to update then we have to add @Transaction and @Modify.
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status")String status, @Param("id") Integer id);

    User findByEmail(String email);//This query implementation we are not writing. As JPA has the logic to generate the right query.

}
