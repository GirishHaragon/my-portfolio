package com.blog.exception;

import com.blog.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice//In order to tell that this is a catch block we have to apply this @ControllerAdvice Annotation. //So what this anno does is, any exceptions that occurs in our project, will directly be suppressed in here. So this class acts as a Catch block. //So @ControllerAdvice anno receives the exceptions that occurs anywhere in the handler method of our project, wherever the exception occurs in our handler methods that has to come here.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //The class we have created in the name of Global Exception handler but the below method is created to perform Specific Exception Handling which is ResourceNotFoundException.
    @ExceptionHandler(ResourceNotFoundException.class) //Here we r specifying any exception related to ResourceNotFound, that exception will first taken by this @ControllerAdvice and in that class it goes to this method. Whether it is Post not found, Comment not found or any exception occurs related to resource not found object will firstly go to @ControllerAdvice and in that it'll go to @Exceptionhandler because we have given the class name very clearly. //Whenever post is not found, we r using lambda's expression as 'new ResourceNotFoundException()' postNotFoundWithId when that object is created it will go to this method and this method will take object address into 'ResourceNotFoundException exception', like how in catch block we used to write try/catch (exception e), in this e is a reference variable as it is 'exception' here is a reference variable and 'Exception' is a exception class name which could handle any kind of exceptions there. Which means in our controller, when an exception occurs, which is 'ResourceNotFound' it will come here and 'ResourceNotFoundException exception' will hold that excp objet's addr and it'll suppress the excp. So it acts like a catch block.
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException( //This is a Specific exception because Resource Not Found exception will occur when the entered postid or commentid is wrong or postId & commentId doesn't exist, then we have already thrown ResourceNotFound Exception for that case. Now to handle such excp we build this specific excp. Because we know that when to throw this exception & handle, therfore we build specific excp for this. //In order to do that we r building a method whose ResponseEntity type is ErrorDetails, which we created earlier that consists, timeStamp, message & details. These 3 things we r returning back to the user through response section,, //ResponseEntity which returns response back to Postman/client.
                                                                         ResourceNotFoundException exception, //We need to give/specify what exception this method should be catching/handling, in this method parameter section we need to give first that class name.
                                                                         WebRequest webRequest) { //As a part of Exception handling ensure that we have WebRequest present inside the method parameter..
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));//Here we created the error details object & return the object status. //So now what we have done is we have initialized the error details constructor by supplying the 'Date', 'exception.getMessage()' and the 'webRequest.getDescription' right now we kept it false. //So there r lot of methods in 'WebRequest webRequest' that can help us to configure messages.
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND); //Now we r returning the Response back, telling HttpStatus.Not_Found. And 'errorDetails' will have the 'Date' & the 'exception.getMessage()'. //We can remember in catch block, we used to write 'ArithmeticException e' & in that SOP of 'e'. Then what e would print is Exception message. So here (ResourceNotFoundException) this is where your excep object is. When we write 'exception.getMessage()' it will get the message of excep.
    } //So whenever we want to build an Exception class, firstly (1st Step) develop a class, apply @ControllerAdvice anno and make it a subclass of ResponseEntityExceptionHandler. Secondly (2nd Step), develop a method with the anno @ExceptionHandler, and give the class name for which this method is responsible to handle exception, this method is responsible to handle exception only for the class we mention in the method param (ResourceNotFound.class) and we give 2 params in this method which the other one will be 'WebRequest webRequest'. //In the 2 params where as class (ResourceNotFoundException excp) where in we r giving that object reference variable so that when the object address goes to this reference, and the method will suppress the excep firstly. Second we have the WebRequest, which has lot of info which can be used to send it back to the client/Postman. ResponseEntity of the method returns the response back to Postman.

    //When we create post first time it gets saved but when we try to save the post again with all details to be as it is, then the app and response get crashes, because of 'DataIntegrityViolationException', duplicate titles are not allowed as per our coding. And this exception cannot be handled by the 'ResourceNotFoundException'. So this will become Global Exception case.
    //Global Exception.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception exception, //Now Exception class can handle any exceptions. So this become Global exception handler method which can handle all exceptions not just specifically.
            WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}