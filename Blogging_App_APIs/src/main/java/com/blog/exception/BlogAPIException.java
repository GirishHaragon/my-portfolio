package com.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {
    public BlogAPIException(String message) {
        super(message);
    }
}
