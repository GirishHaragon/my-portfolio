package com.blog.service.impl;

import com.blog.entities.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.payload.PostResponse;
import com.blog.repositories.PostRepository;
import com.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    //All these while we have used the dependency injection using @Autowired, but in the recent versions there is something called as Constructor Based Injection (Which is an Interview question). Instead of writing @Autowired there are other ways to inject the bean. Like,
    private PostRepository postRepository;
    //To inject the bean, we just right click & go to Generate->Constructor & click ok. No need of @Autowired here. Just by writing above 1line & below 2lines dependency injection will happen. This was later introduced in SB. Most of the developers prefer this. Constructor Based Injection. That will automatically create a bean & inject into it.

    //As we are using ModelMapper, the above postRepository & it's constructor written below, we r adding with creation of ModelMapper bean.
    private ModelMapper mapper;//I have manually downloaded the maven dependency from Project Structure from File menu. Actually we need to type the dependency tag and import the library in MVN repo. //But now this is not a built-in class of spring boot, it is a JAVA Library downloaded & this Construction based Injection will not work. But still if we use, then appl is going to crash. @Autowired will work on this bcs Repository layer that we developed belongs to springframework (Importing JPARepository from) so this becomes part of spring library(built-in), so for postRepository the @Autowired will work. But we imported ModelMapper from modelmapper lib, it doesn't belong to spring, so here CBI will not work. Because it is an external lib, bean creation springIOC will not be able to do, as SpIOC doesn't have the details about which object to create & inject the address into it. So there are two kinds of libs we r seeing here 1.Built-in Lib, 2.External Lib. //As we have configured the Bean type by creating a method in Config Class (BlogApplication.java). After this SpringIOC will have the detail to create object of ModelMapper and it's object addr, it'll store into this variable (mapper).

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {

        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {//Here we have the DTO, we need to take the DTO & save that into DB, But DTO can't go to DB. What goes to DB is Entity. So we will firstly create an entity object Post.

        Post post = mapToEntity(postDto);//Here we are calling the mapToDto method. Advantage of putting the code in the method is that code will become reusable, whenever we want to convert entity to dto, don't repeat the code, call the method, that will automatically handle, the conversions. Now we take this into post. So whatever comes to this 'postDto' in thi line that goes to the mapToEntity method that 'postDto' in entity method converts that into postEntity returns back to 'post' of this line, and we r saving it in the next line of code. & then convert that savedPost into DTO, for that we will build another after mapToEntity. This was simple, We just call the method(mapToEntity), one Dto object converted to one Entity object. This was simple, Call the method once it converts that into Dto & vice versa. But we got more than 1 object in DB, We need to convert all the records to Dto & Entity, that's where Stream API comes to picture, in getAllPosts() method.

        //Here we get the data, that goes to the postDto, Now we will save the data,
        Post savedPost = postRepository.save(post);//Once we save, actually save method has a return type which we did not explore earlier. Once we save the record, just place the cursor before postRepo and press Alt+Ent & Introduce a new variable. It will return back savedPost variable.
        //Whatever post content is saved in the DB, that savedPost content will be present in the new object. So what save method does is, it not only saves the data in DB after saving the data successfully, what data is saved it will put that into Post Object. Now Convert this object back again to DTO, because the method return type is PostDto. so once we save & you have Entity, convert this entity back to Dto. So create an Object of the return type, PostDto

        PostDto dto = mapToDto(savedPost);//We r now going to call the method here, & to this method we will supply savedPost. Alt+Ent will return PostDto & we will save it to variable dto. This is just simple, We just call the method(mapToDto), one Entity object converted to one dto,

        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {//This is where we have to fetch all the record from the database.Now we r going to use the repository layer and have findAll()

        //Now here we will use Ternary Operator (Java concept), instead of if-else condition we use TO, works same as if-else.
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? //If we give in the URL as sortDir=asc then the statement "equalsIgnoreCase(Sort.Direction.ASC.name())" becomes true. If this line here is true then the 'sortDir' is Ascending then condition 1 runs & if statement goes false then sortDir becomes descending & condition 2 runs.
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); //Now we will paste the 'sort' variable in place of 'Sort.by(sortBy)' in below line. That's it of sorting.

        //So now here something called Pageable.
        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort); //After this we will paste the pageable in findAll(), but then the return type of findAll will not be List. //When we write of() with (int pageNo, int pageSise, Sort sort) that gives error so we have to convert String sort to Sort sortBy. Now we use "Sort.by(String properties..)" function to convert String object to Sort[as Sort.by(sortBy)
        Page<Post> content = postRepository.findAll(pageable);//Now we have all the posts, but we cannot return Entity object, we have to return dto object for security.
        //Now we convert that return type Page into List by applying getContent(0 method.
        List<Post> posts = content.getContent();//Here we changed the variable name posts to content.. And the new variable for List we give it as previous posts.
        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();//These all built-in methods are something that we r getting from the Page<> object.
        postResponse.setContent(dtos);//Here dtos has all the post content.
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setTotalElements(content.getTotalElements());
        postResponse.setIsLast(content.isLast());

        return postResponse;//We are changing the method return type here from List<PostDto> to PostResponse & as well as return here too. In the Service layer too we should change it to PostResponse. & for other errors we should change. //Or instead of this line, we can also write the above line as instead of writing 'List<PostDto> postDtos = ' we can replace it with 'return' there itself, no necessary to give new variable. That will automatically return the list of dtos.
        //So now what is happening, when we call mapToDto method that will convert the entities to dto, & now all that dtos we r returning back to PostController (getAllPosts()) method.
    }

    @Override
    public PostDto getPostById(long id) {
        ///Optional<Post> byId = postRepository.findById(id).get();//Now there is null. Let us read the value.//Once it Finds the record we get the return as Optional Class for this we will apply get(). But now we will not directly get the record. So Firstly check whether the post is there with the Id. Then only we should get the record. If the Post with the Id does not found then we should handle it with exception class, so let us create object of the Custom Exception class.
        Post post = postRepository.findById(id).orElseThrow(  //Here instead of using throw(Insufficient Fund - throw exception) keyword, we will use 'orElseThrow()' & in that, we will write '()->' & create object of 'new' 'ResourceNotFoundException()' with a message '("Message"+id)'.  Now what this does, If the record is found, then that record is found in post object. & If the record is not found then it goes to the orElseThrow() part. So instead of using throw() keyword this is more easy way of doing it with less no of lines of code. orElseThrow function acts as if-else block.
                () -> new ResourceNotFoundException("Post Not Found with id: " + id)//When this object is created, it will call the constructor in ResourceNotFoundException which has a super keyword in it. & that will take care of displaying this message in Postman. But understand it is only throwing exception, not handling. Handling we'll see later.Bcs throw keyword only throws exception not handles.
        );//Now we have the record in 'post' variable, convert the post to dto.

        PostDto postDto = mapToDto(post);////Now we have the record in 'post' variable, convert the post to dto. & supply post to mapToDto(). & this will return back a dto object to us. & we return a dto.

        return postDto;//Why we created exception class (CustomException), so that in this scenario, when the record is not found, let us throw a custom exception. //Now we need to return this in ResponseEntity in Controller layer.
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) { //Remember, do not directly update the post, firstly check does the post exist, if not display 1 message "PostNotFound with this Id", if the post exist then update the post //Now this is going to give us our services layer, then after we will update the record, by updatePost() method.
        Post post = postRepository.findById(id).orElseThrow(

                ()->new ResourceNotFoundException("Post not found with id: "+id)

        );//If the record with the id is found then record will be updated into(Post post). If not found, then lets create 1 exception object bot by using throw keyword, & using Lambda expression.

        //So now we have got the post by the right id, let's update the record. To update the record, content is present in postDto(in meth arg), take this content & update this(post) object.
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        //Now we have the updated info from dto being set into post, now we save the post.
        Post updatedPost = postRepository.save(post);//Whatever Post is updated that info is present in updatedPost. But the service layer cannot return Entity, it has to return Dto. So to convert entity to Dto, we already have a method mapToDto() where we just give this updatedPost to it, which will return back a dto. To club the steps shorter we type return infront of this, to get the dto in return, that goes back to controller dto (in updatePost() method)

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {//Do not directly delete record with any id. First we have to check if the Post with id no exists or not. Then we will delete the record. For checking the existence of id we will copy the same code we used in updatePost method, which will help us to throw custom exception if the post doesn't exist.
        Post post = postRepository.findById(id).orElseThrow(

                ()->new ResourceNotFoundException("Post not found with id: "+id)

        );
        postRepository.deleteById(id);
    }

    Post mapToEntity(PostDto postDto){//When we give the DTO object, it will convert that into Entity, & we will just return back the entity object (post).

        //We are applying ModelMapper concept to copy the data from dto to entity & vice versa. Therefore we have commented the below 4lines of codes.
        Post post = mapper.map(postDto, Post.class);//That's it, so this method will automatically copy the data from postDto to Post.class //By this one line all the below redundant code is gone. //So what we need to map is 'postDto' from 'mapToEntity(PostDto postDto)' and what we need to map to is 'Post' from 'Post mapToEntity(PostDto postDto)' with this we need to give/specify the .class extension.

//        Post post = new Post();//We'll copy the data from DTO to entity, bcs what goes to the DB is Entiyty, not DTO.
//        //How to copy the data from Dto to the object created,
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;//This method we need to call it.
    }

    PostDto mapToDto(Post post) {
        PostDto dto = mapper.map(post, PostDto.class);//So what we need to map is 'post' from 'mapToDto(Post post)' and what we need to map to is 'PostDto' from 'PostDto mapToDto(Post post)' with this we need to give/specify the .class extension.
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());//And then we return this dto variable. That's what sir mentioned us in the diagram / flow. Now the service layer, after saving the Entity is returning back Dto to Controller. so we build the controller.
        return dto;
    }
}