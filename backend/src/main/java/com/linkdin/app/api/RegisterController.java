package com.linkdin.app.api;

import java.util.HashMap;
import java.util.Map;

import com.linkdin.app.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @PostMapping(path = "/register")
    public ResponseEntity <String> register(@RequestBody User user) {
        System.out.println("BBBBBBb");
        return ResponseEntity.ok(user.getUserName());
    }
}
