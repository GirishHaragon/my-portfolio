package com.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);//When the record is not found, we will create object of new, resource not found exception & to the constructor we will supply "Post with ID-2 not Found". This will automatically print that in Postman Response.d
    }//When object is not found orElseThrow() will run, & when the message goes from PostServiceimpl getPostById() to the super keyword, automatically that message Spring Boot will display in PostMan. We just give the Constructor with super keyword message to the Parent Class (RuntimeException) rest SB will display that in Postman.
}