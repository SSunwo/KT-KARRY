package com.kt.karry_backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.karry_backend.entity.Registration;
import com.kt.karry_backend.entity.User;
import com.kt.karry_backend.service.UserService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsersAll() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<?> postUser(@RequestBody User resource) throws URISyntaxException {
    
        User user = userService.addUser(resource);

        URI location = URI.create("/users/" + user.getUser_num());
        return ResponseEntity.created(location).body("creation complete");
    }

}
