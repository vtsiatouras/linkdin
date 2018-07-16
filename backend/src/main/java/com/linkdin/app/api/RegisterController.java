package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserRegister;
import com.linkdin.app.model.User;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity <String> register(@RequestPart("user") String jsonUser,
                                            @RequestPart("profileImage") MultipartFile profileImage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserRegister userRegister = objectMapper.readValue(jsonUser, UserRegister.class);
            System.err.println(userRegister.email);

            // Check if email is already stored
            if (userService.emailExist(userRegister.email)) {
                System.out.println("EMAIL EXISTS");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Check if the passwords are not matching
            if (!userRegister.passwordCheck(userRegister)) {
                System.out.println("PASSWORDS DO NOT MATCH");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // TODO STORE THE IMAGE
            try {
                String content = new String(profileImage.getBytes());
                System.err.println("IMAGE\n" + content);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Store new user
            User user = userRegister.transformToUser(userRegister);
            userService.storeUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
