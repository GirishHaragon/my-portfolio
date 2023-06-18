package com.blog.repositories;

import com.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {//We give here Entity class(Comment) & wrapper class (Long/long) to JPARepo.

    ///Comment findByEmail(String email);//We have built an incomplete method and made the return type as Comment. Now to this method if we just supply 'email' Spring Boot will automatically search for the record based on the email id. Logic building SB will do. But the meaning of the method name should be meaningful (findByEmail). So SB knows that it has to go to repository layer, there is a column email, in that column find the record based on emailId

    //Let's say we want to find the record based on name,,
    ///Comment findByName(String name);//That's it. Sp now when we call this method & to this method when we supply name, SB will automatically search the record based on name.

    //That is Magic of SB. We are not even building the logic. Just we are commanding controller that, Just go to Repository layer give the method name, now this method will automatically build an SQL query internally using SpringJPA, "select * from comment WHERE name=smith@gmail.com".
    //So let's say we want to find the record based on PostId.
    List<Comment> findByPostId(long postId);//The variable 'long id' can be anything, to not confuse with the DB table column names we change it to 'postId', so there are 2 columns in DB with id, to avoid confusion we go with its 'postId'. And we cannot just write Comment in the return, bcs for 1 postId we have many comments, so we get more than 1 record, therefore we will take return into List of Comments. So if we just give the postId 2, it'll automatically go to DB, it'll fetch both the comments, & that comment it will store it in the List<Comment>
    //After we go to CommentService, & we will build a method getCommentsByPostId.

}