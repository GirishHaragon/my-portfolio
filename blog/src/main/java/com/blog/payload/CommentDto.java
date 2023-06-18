package com.blog.payload;

import com.blog.entities.Post;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String body;
    private String email;
    private String name;
}
