package com.linkdin.app.api;

import java.util.HashMap;
import java.util.Map;

import com.linkdin.app.dto.UserDTO;
import com.linkdin.app.model.User;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity <String> register(@RequestBody UserDTO userDto) {

        System.out.println(userDto.getFirstName());
        System.out.println(userService.findUserByEmail(userDto.getEmail()));

        return ResponseEntity.ok(userDto.getFirstName());
    }
}
