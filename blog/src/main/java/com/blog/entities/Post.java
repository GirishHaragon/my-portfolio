package com.blog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data//This ann will help us with generating getters & setters automatically.
@AllArgsConstructor//This will automatically create a constructor with arguments.
@NoArgsConstructor//This will automatically create a constructor without argument
@Entity//To specify Entity class
@Table(name="posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})//To create/update a table with a different table name in DB.

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//To make it primary key.
    private Long id;

    @Column(name="title", nullable = false)//To make the title unique we can write in argument here as 'unique=true' & there is another way where we write im Table ann argument
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="content", nullable = false)
    private String content;//Whenever we write a post, it should have a title, description & content in it. Three parts we will be writing in our blog post.
    //But in Entity class we were using getters & setters, but when we use Lombok annotation we need not use getters & setters. From Lombok automatically getters & setters are made available.

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)//CascadeType means in Hibernate that defines the set of operations that should be cascaded from a parent entity(Post) to its associated child entities(Comments). When user deletes a Post then all the related comments get cascaded/removed automatically. //Using "FetchType.LAZY" can improve the performance of your application when dealing with large datasets, as it avoids fetching unnecessary data in the Cache memory from the database. It also reduces the amount of memory usage in the application, as only the required child entities are loaded into memory when needed. We can use FetchType.eager to load all the data to cache at once even if not necessary at the moment. //In Hibernate, "orphanRemoval=true" is an option that specifies whether an associated child entity should be removed from the database when it's no longer referenced by the parent entity.
    private Set<Comment> comments = new HashSet<>();//We will create a comment class in entities package. Then we will import Set class.
}