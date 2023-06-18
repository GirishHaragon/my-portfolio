package com.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data//Instead of this anno we can also use @Getter & @Setter annos, but in the latest version they have just given one anno, sometimes @data will not work bcs of some version issues, then we can use the other annos.
public class PostDto {//This should exactly have the same variables as that of entity class.
    private long id;
    // title should not be null or empty
    // title should have at least 2 characters
    @NotEmpty//In this anno, if we don't give any message it takes the default message as "must not be empty". But if we want we can add the message. We have added message for content.
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;//Now after applying validation annotations the title should have minimum 2 characters. If it will be 1 character then it will display the message in Postman.

    // post description should be not null or empty
    // post description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;//By the anno NotEmpty the field cannot be left empty. And Size validates the minimum size of the description.

    //Post content should not be null or empty
    @NotEmpty(message = "Content should not be empty")//Now it will give the custom message.
    private String content;
}
