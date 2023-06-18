package com.blog.payload;

import lombok.Data;

import java.util.List;

@Data//To generate Getters & setters.
public class PostResponse {
    private List<PostDto> content;//We needed actually content here but as content is a part of our Entity so we don't expose entity to the end user. So controller should always return Dto objects to the Postman therefore we supply PostDto to the List. And we will apply encapsulation & make the values private
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private Boolean isLast; //The last page will return true/false  //These r what the Angular team/React team/Android/IOS teams is expecting from us API Developers. That whenever they get the datas, they want these informations in responses, to make use of the info & do the frontend.
}
