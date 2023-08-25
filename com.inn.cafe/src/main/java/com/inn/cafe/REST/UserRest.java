package com.inn.cafe.REST;

import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {

    @PostMapping(path = "/signup")//We are creating a signup/creating a user so we use PostMapping HttpMethod.
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);//There is no need of writing public here as the interface methods are by default abstract..

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/checkToken")
    public ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);

}