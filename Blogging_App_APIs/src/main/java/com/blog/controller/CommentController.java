package com.blog.controller;

import com.blog.payload.CommentDto;
import com.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")  //This is the URL which will trigger this class.
public class CommentController {

    private CommentService commentService;//We have created an object of Interface CommentService with the constructor for calling it in the below methods.

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments   //By looking at the URL we can understand that, after the post/ if there is a no, that is the postId, and after the comments there is a no, then that is commentId. URLs that we develop should be meaningful, like we r building OneToMany mapping, build the URL like this.
    @PostMapping("/posts/{postId}/comments")//Because we r creating a comment we give Postmapping
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") long postId, //We r reading this thing by the URL. For this PostId we need to update the comment. So for this we meed to call the service layer above
            @RequestBody CommentDto commentDto
    ){

        CommentDto dto = commentService.saveComment(postId, commentDto);//Here we r using CommentService object, and the method name we have given is saveComment() & to that we supply postId & commentDto variables. //So the flow will be like, this postId & commentDto will go to the method argu in the CommentServiceImpl, for that id we r checking the post exists or not by findById(), if exist then gets into the 'Post post' orElsetThrow() will work. Then we create a 'Comment comment = mapToEntity(commentDto);' to convert/copy the data from commentDto to Entity(By creating a new 'mapToEntity()' method). And then we set the Post by 'comment.setPost(post);' so that for this post we save the comment & then save the comment by 'Comment newComment = commentRepository.save(comment);'. And then we return back a dto object by 'return mapToDto(newComment);' (With the help of 'mapToDto()'. That dto we r returning back to that saveComment() method, we will take that into CommentController dto variable as 'CommentDto dto = commentService.saveComment(postId, commentDto)' & then we will type here return statement as 'return new ResponseEntity<>(dto, HttpStatus.CREATED);'

        return new ResponseEntity<>(dto, HttpStatus.CREATED);//Now let's test this.
    }

    //Here we will build a method, & to that we will supply the postId no.
    //So we are creating a handler method in the CommentController; So these r all called as handler methods.
    //http://localhost:8080/api/posts/1/comments  //The url will be similar, but bcs we r getting the record we use @GetMapping
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentDto> commentDto = commentService.getCommentByPostId(postId);//So the flow will be like, when we enter the url the postId from url (via @PathVariable) will go to 'long postId' in this method argument.
        return commentDto;//We can also replace the above code (List<CommentDto> commentDto =) with return keyword there only. Now after this let's test our application.
    }

    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") long postId, @PathVariable("commentId") long commentId) {
        CommentDto dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/{postid}/comments/{id}
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id,  //As sir had not used here commentId in place of id
            @RequestBody CommentDto commentDto//We will also keep here one commentDto object bcs whatever data we want to update, to the URL we supply JSON object. And that JSON object content needs to be updated in DB. For this we use @RequestBody, bcs it takes the content of JSON & it will put that in commentDto object.
    )
    {
        CommentDto dto = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment( //Here why String is bcs we just want one String message "Comment is Deleted".
                                                 @PathVariable("postId") long postId,
                                                 @PathVariable("id") long id
    ) {
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Comment is Deleted", HttpStatus.OK);
    }
}