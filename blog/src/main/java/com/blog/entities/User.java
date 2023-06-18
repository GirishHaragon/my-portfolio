package com.blog.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data//We r applying @Data for getters & setters.
@Entity//@Entity which is Hibernate JPA annotation.
@Table(name = "users", uniqueConstraints = {//@table to create table,
        @UniqueConstraint(columnNames = {"username"}),//We r making these two things unique, username & email has to be unique otherwise with the same username & email 2 2 times registrations will happen or Signups will ahppen.
        @UniqueConstraint(columnNames = {"email"})
})
public class User {//Here we have the user Entity.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;//This matches with the ER diagram table.
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//When we write ManyToMany the first many is for user, and second many is for roles. //FetchType is Eager which means when we start our project all our tables are loaded into the memory before even there is a requirement of these tables in our project. //But the Lazy FetchType will load only those tables which is required at the given point of time.
    @JoinTable(name = "user_roles",//So user table is mapped with role table using JoinTable annotation. So this is our third table which will automatically get created . And we r joining those with the help of Columns user_id referring to the id of user table & role_id is referring to the id of Role table.
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), //This means the user_id is mapped to the id of user table. //JoinColumn is between Parent table & third table.
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) //The role_id is referred to the id of Role table. //InverseJoinColumn is Child table & that third table.
    private Set<Role> roles;//Bcz it's many to many, the Role is defined here as a Set, if it is one to many then One will not be Set, many will be Set/List. If it is many to one, then one will not be Set/List. //When to use Set & list? -> List can have Duplicate values. But Set will not have duplicate value. //Since we don't want any duplicate values here we used Set. The Role here will be Unique.

    //Now we modify the code a bit using ChatGPT
    //Now let's say whenever we create a user we need the creation date and user updation date which is not there.
//    @CreationTimestamp //This will take care of auto generation of creation date.  //This anno will automatically inject the Date & Time when the user is created..
//    @Column(nullable = false, updatable = false)//I am removing the text "name = "created_at"," bcz it is creating a column updated at in the users table of DB.
//    private LocalDateTime createdDate;
//
//    @UpdateTimestamp //This will take care of auto generation of updation date.  //This automatically update the time when the user updates his password, email..
//    @Column(nullable = false, updatable = false)//I am removing the text "name = "updated_at"," bcz it is creating a column updated at in the users table of DB.
//    private LocalDateTime updatedDate;///////One thing to remember, once created table can be updated only if new columns or rows are to be creating and cannot update for removing columns of a table.

    //After this we create the repository layer for this entity
}