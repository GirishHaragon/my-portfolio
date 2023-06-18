package com.blog.payload;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;//We import the date by util package.
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {//This is Constructor Based Injection.
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp(){//We have Generated Getters & Setters.
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
