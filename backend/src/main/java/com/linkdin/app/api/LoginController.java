package com.linkdin.app.api;

import java.util.HashMap;
import java.util.Map;

import com.linkdin.app.dto.UserDTO;
import com.linkdin.app.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDto) {
        System.out.println(userDto.getFirstName() + " " + userDto.getPassword());
        return ResponseEntity.ok(userDto.getFirstName());
    }
}
