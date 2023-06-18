package com.blog.service;

import com.blog.payload.PostDto;
import com.blog.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);// createPost will take PostDto object. What goes to the service layer is a Dto object, from the controller layer. Alt+Ent import for PostDto. The Service layer will return PostDto.

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePost(long id);//Build a method with return type void
}