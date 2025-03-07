package com.kt.karry_backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.karry_backend.entity.User;
import com.kt.karry_backend.service.UserService;


@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    // private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 아마도 회원가입 api
    @GetMapping("/users")
    public List<User> getUsersAll() {
        return userService.findAll();
    }

    // test용
    // @PostMapping("/users")
    // public ResponseEntity<?> postUser(@RequestBody User resource) throws URISyntaxException {
    
    //     User user = userService.registerUser(resource);

    //     URI location = URI.create("/users/" + user.getUserNum());
    //     return ResponseEntity.created(location).body("creation complete");
    // }

    // 회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws URISyntaxException {
    
        User newUser = userService.registerUser(user);
        
        // 회원가입 성공하면
        Map<String, Object> response = new HashMap<>();
        response.put("message", "SignUp successful");
        response.put("user_num", newUser.getUserNum());
        response.put("user_id", newUser.getUserId());

        return ResponseEntity.ok(response);
    }

    // 로그인 api
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> postLogin(@RequestBody Map<String, String> paramMap) throws URISyntaxException {
    
        String userId = paramMap.get("user_id");
        String userPw = paramMap.get("user_pw");

        // 유저 조회
        Optional<User> userOptional = userService.findByUserId(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "User not found"));
        }

        User user = userOptional.get();
        
        // 비밀번호 검증
        if (!userPw.equals(user.getUserPw())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invaid Credentials"));
        }

        // 로그인 성공한다면

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("user_id", user.getUserId());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

}