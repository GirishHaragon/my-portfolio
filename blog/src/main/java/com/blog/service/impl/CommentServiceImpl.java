package com.blog.service.impl;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exception.BlogAPIException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.PostRepository;
import com.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper) {//We could have used @Autowired over the above 2 lines. Constructor Based Injection.
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto saveComment(Long postId, CommentDto commentDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + id)
        );//We r checking if the post exist or not, there is no point in saving comment when post doesn't exist. //Now in CommentDto Dto cannot be directly saved to DB, we need to convert it to Entity, then save it. To convert dto to entity we build the method mapToEntity()

        Comment comment = mapToEntity(commentDto);//Now we take the above CommentDto & pass-on to the below method(mapToEntity). This will return us back Comment object.
        comment.setPost(post);//We are missing one thing/step that is, the comment should be set for this post. //We r doing this bcs we want the comment to be set for this post only. //So first of all we need to specify that this comment is meant for this post.
        Comment newComment = commentRepository.save(comment);

        //Now take the newComment and call the method.
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(Long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);//In the CommentRepository we have developed a method findByPostId, we are making its implement here. We have already created a commentRepository object in here, we r using it here & the method we developed in CommentRepository (findByPostId()) to this we give postId. Now automatically based on the postId SB will return us th List of Comments. Now convert the comments to CommentDto. For this we use Stream API concept.
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());//toList will give us list of Dtos. Now we need to call this from controller layer. So Controller Layer has to Call that.
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        ); //This will find the post by postId.

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment Not Found with this Id: " + commentId)
        ); //This will find the comment by commentId.

        //Then we should check that does the comment exist for the particular post or not?,,, We should check if for the above comment belongs to the above post then only it should fetch the record, otherwise we give them BlogAPI exception, Internal Server error, because this post does not match with this comment.
        //In this condition '!comment.getPost()' is comment id. And post id is 'post.getId()'. //comment.getPost().getId() means controller will go to the comments table (by comment.), in that it refers to the PostId which is a foreign key (by '.getPost().' which refers to post_id column of comments table), then '.getId().' means the primary key id in comments table or the comments id. //And 'post.getId()' is referring to the primary key postId (Id) in Posts table of DB. //Here we are comparing '!comment.getPost().getId().' with 'post.getId()' by equals function. That means the comparison between comment Id (Primary key - comments table) & postId (foreign key - comments table) with post Id (Primary key - Post table). //So we r comparing now ID in comment table for the POST foreign key & the ID primary key in the Post table, are they basically not equal.
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException("Comment does not belong to Post"); //If 1 equals 1 is true, then this exception shows up. If 1 not equals to 1 false, then it'll not throw this exception. //To solve the BlogAPIException we press Alt+Ent, and choose Exception package, then hit ok, then in that we change the inheritance of that class to extends RunTimeException and then in the constructor arg we supply String message and to that we give super(message). And that super message will be written here without HttpStatus code.
        }
        //Now if everything is ok, then we should get return mapToDto.
        return mapToDto(comment);//Since we return back here the comment object which id converted back to mapToDto. And in the method the return type should be only CommentDto not List of CommentDto, bcs we r not returning List here, but we r returning only one comment. So the changes should be applied in Interface (CommSerImp)
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        //Here is where we will write the logic to update the comment.
        //From this we r getting postId (primary key) from Post entity. let's assume postId=2 (by post.getId()).
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not Found with id: " + postId)
        );

        //From this we r getting postId (foreign key) from Comment entity. let's assume post_id=2 (by comment.getPost().getPostId()). If this post_id and the PostId r both same then true in next if block and exception doesn't appear. and if both r different then comment doesn't belongs to the Post, if block runs & throws BlogAPIException.
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment not Found with id: " + id)
        );

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException("Comment does not belong to Post");//This is another way to throw an exception instead of Lambda's expression.
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);//This will help us to convert entity to dto. And this will be returned to controller layer.
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        ); //This will find the post by postId.

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment Not Found with this Id: " + id)
        ); //This will find the comment by commentId.

        //Then we should check that does the comment exist for the particular post or not?,,, We should check if for the above comment belongs to the above post then only it should fetch the record, otherwise we give them BlogAPI exception, Internal Server error, because this post does not match with this comment.
        //In this condition '!comment.getPost()' is comment id. And post id is 'post.getId()'. //comment.getPost().getId() means controller will go to the comments table (by comment.), in that it refers to the PostId which is a foreign key (by '.getPost().' which refers to post_id column of comments table), then '.getId().' means the primary key id in comments table or the comments id. //And 'post.getId()' is referring to the primary key postId (Id) in Posts table of DB. //Here we are comparing '!comment.getPost().getId().' with 'post.getId()' by equals function. That means the comparison between comment Id (Primary key - comments table) & postId (foreign key - comments table) with post Id (Primary key - Post table). //So we r comparing now ID in comment table for the POST foreign key & the ID primary key in the Post table, are they basically not equal.
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException("Comment does not belong to Post"); //If 1 equals 1 is true, then this exception shows up. If 1 not equals to 1 false, then it'll not throw this exception. //To solve the BlogAPIException we press Alt+Ent, and choose Exception package, then hit ok, then in that we change the inheritance of that class to extends RunTimeException and then in the constructor arg we supply String message and to that we give super(message). And that super message will be written here without HttpStatus code.
        }

        commentRepository.deleteById(id);//As this method is void, we don't take this into any variable & also not returning any value.
    }

    Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setBody(commentDto.getBody());
//        comment.setEmail(commentDto.getEmail());
//        comment.setName(commentDto.getName());
        return comment;
    }

    //Now once the comment has been saved, next we need to convert entity object back to dto. To do this we simply copy the same code where we converted dto to entity. Just change the Entity to dto & vice versa, and some more changes.
    CommentDto mapToDto(Comment comment) {
        CommentDto dto = mapper.map(comment, CommentDto.class);
//        CommentDto dto = new CommentDto();
//        dto.setId(comment.getId());
//        dto.setBody(comment.getBody());
//        dto.setEmail(comment.getEmail());
//        dto.setName(comment.getName());
        return dto;
    }
}