package com.inn.cafe.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email=:email")
//u is the alias for the table name. The User should be matching or else exceptions occur.
//We are passing the email to the method present in repo/dao.

@NamedQuery(name = "User.getAllUser", query = "select new com.inn.cafe.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) from User u where u.role='user'")
//We r writing query for getAllUser method in DAO therefore name="queryBelongingClassName.methodName", query="what form of output we want is written here as a form of new object of type UserWrapper class() then from then table name User and any variable for it then where variable.role='user' because we want only user role's details not admins.

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'")

@Data//This anno provides 0Arg constructor for our application.
@Entity
@DynamicInsert//In CRUD operation video he has already explained what are these..
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;//Have to check in ChatGpt, why we use?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;//EmailId is itself the username for our project.

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    //After developing this POJO/Entity we check by starting the app whether the sql query is showing & formatted/not..
    //For the SQL to be seen and in formatted way then the 2 statements are needed in the properties file.
    //After creating the table we don't see the table creation query again being generated.

}
