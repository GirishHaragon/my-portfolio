package com.blog.controller;

import com.blog.payload.PostDto;
import com.blog.payload.PostResponse;
import com.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")//When u give the api url, start with api.
public class PostController {

    private PostService postService;//We will do Constructor Based Injection

    public PostController(PostService postService) {//Created Constructor for the above service object created.
        this.postService = postService;
    }

    //http://localhost:8080/api/posts    //When we enter this URL in postman, this method will be called, this method will then take JSON content into Dto(postDto). This controller layer takes the Dto(postDto) & calls the service layer(postService -> PostServiceimpl), service takes the Dto content(postDto), converts that into Entity, saves the Post, then after saving, what is saved it again gives an entity(Post), we convert an entity back to Dto & we r returning back dto to the controller layer & we r saving it here (PostDto dto = postSe..). Now this dto we need to display that in Postman (In Postman we should have dto as response).
    @PreAuthorize("hasRole('ADMIN')") //By applying this anno over PostMapping we configured the access only to the ADMIN user when logged in as Admin. Not USER user. Only an Admin can create Post or access this method PostMapping. Other users cannot. We should not byHeart, but have to remember the steps.
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult result) {//this will have the method that will help us with do postMapping. We will not make it's return type void (Like we r not writing public void here bcs void means it returns void) instead writing ResponseEntity, now createPost will take the data into PostDto & apply @RequestBody on it bcs requestbody from JSON will take the content from JSON & put that in DTO. Explaination : ResponseEntity<PostDto> :- In PostMan we should actually get the response code in response section (Response http status code). In projects when they give us the requirement, they tell "When we save a record - It should return a status code". There r diff diff Status codes, when we create a record it should return a status code 200 - (when we delete, update or read the data), 500-internal server error (when something goes wrong),400-(Bad Request), 401-Unauthorised access (When access is denied), 404-(When the Url formed is incorrect). @@Interview Que@@ -> Tell me when you creating a record what status code would you expect back?@@ So This status code & response to send back to PostMan we change the retun type of method in Controller to ResponseEntity<Post>

        //If any errors are present as per the Validation,
        if(result.hasErrors()){//This block was missing in the word notes & I added it extra.
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.createPost(postDto);//The flow starts from this method. And Ends at this line returning (PostDto dto = postSe..)
        return new ResponseEntity<>(dto, HttpStatus.CREATED);//We written ResponseEntity objective create, supply the dto & supply HttpStatus code created when we select created, it'll give the status code 201, so whenever they(Interviewer) give us a requirement document, in that there will be a table, when you build this api, this api should return this particular status code.
    }

    //http://localhost:8080/api/posts?pageNo=1&pageSize=5&sortBy=title&sortDir=asc  //or sortDir=desc  //This is the URL we will be using in postman, If pageNo & pageSise is not give in URL then by default it will take what we have given in the condition, pageNo as "0" & pageSise as "5". And in argument the meaning of required=false is that its not mandatory to be given(pageNo & pageSise) in the URL, & if we make it true then that will become mandatory.
    @GetMapping
    public PostResponse getAllPosts( //We will now apply Pagination concept for our project. Now the problem with @GetMapping here is, we r getting all the records at once.
                                     @RequestParam(value = "pageNo", defaultValue = "0", required = false)  int pageNo,  //We gave @RequestParam bcs we will supply the values as a parameter. //Now we will supply pageNo & pageSise to the getAllPosts() method. And also to the PostService interface.
                                     @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){//Now we have continued with the project, This below line returns List<PostDto> & let's call that as postDtos, Now we don't have the method(getAllPosts) in postService. So we r creating one & also in PostServiceImpl.
        PostResponse postResponse = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);//We are pausing our project here bcs what needs to be done here is StreamAPI (Java 8 Feature). Firstly we will move out of this Project & learn StreamApi, we'll comeback & implement here. In Interview they may ask "Which all Java-8 Features you have used in your project? Where have you used it?"
        return postResponse;//From the PostServiceImpl we r returning the postDtos here.
        //This is a Guaranteed question in Interview -> "Java-8 Features, have u used it in your project, if yes where have u used it?" definitely they will ask. Most of companies when proj dev happens, the technical flow happens to be the same almost.
        //We are following MVC architecture all over our project. From Postman we r calling Controller, Controller is calling the Model layer (Service layer in our case), Model layer/Service layer is interacting with the Database.
    }

    //http://localhost:8080/api/posts/{id}    //This is the Path parameter because there is no ? in the URL. If there was a ? in URL, then it would become Query Parameter.Query parameter we will read with @RequestParam & Path Parameter by @PathVarible.
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id) {//Let us read by giving @PathVariable(id). //Now here the return type we will make ResponseEntity of the type <PostDto> //So whatever record we get it now, we will display that back as a dto object.
        PostDto dto = postService.getPostById(id);//Now supply this to the services layer.//Now here also we should get PostDto dto. Now the getPostById method is not there in PostService layer, so we will build it.
        return new ResponseEntity<>(dto, HttpStatus.OK);//Do not put HttpStatus.created, whenever u get the record HttpStatus code should return as 200(We get bcs of .ok). If we write created then 201. Update, fetch the record/reading, deleting - 200 status. Username/passwor incorrect - 401. Not able to detect the method/Url incorrect - 404. Some Internal Logical things went wrong - 500 Internal Server Error, like nullPointerException
    }

    //http://localhost:8080/api/posts/{id}  //In the URL we will give the ID no of Post, that based on this id no find that record & update that record.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable("id") long id) {//& here we will also use (PostDto postDto) which is the JSON object, which will have the information about what needs to be updated, with annotation @RequestBody. And we r writing @PathVariable which will read the id, & will copy that to long id. So what happens is from the url it will pick the id no, from Postman we will submit the JSON object, & JSON object content will go to PostDto postDto. For this id(long id) update the content inside this(PostDto postDto). Bcs without id/any unique Parameter/primary key, no updations can be done. So for this id(long id) the info present inside the object(PostDto postDto), for that id no pick the content from dto object & update the record.
        PostDto dto = postService.updatePost(postDto, id);//Here 2 things we will supply, postDto & id. Lets take the return type of it.
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){  //This time we will see a different requirement, "After we delete a post, we will not return the dto object, we want to return a string message("Post is Deleted!!"). In the response of our postman we want a message, not a dto. So we will enter ResponseEntity<String>, whatever we want to return we will give that in generics(<>). Generics is covered for us in recorded series. Now if we write String in this it will ret str, & if we write dto it'll ret dto. But if we write <?>, it can return anything (Str,dto anything). The return type becomes any return type it can accept.
        postService.deletePost(id);//Create a method in PostService with return type void.
        return new ResponseEntity<>("Post is Deleted!!", HttpStatus.OK);//Once we delete a record we need to return back a string message.
    }
}