package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserRegister;
import com.linkdin.app.model.User;
import com.linkdin.app.services.ImageStorageService;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/register")
    public ResponseEntity <String> register(@RequestPart("user") String jsonUser,
                                            @RequestPart("profileImage") MultipartFile profileImage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserRegister userRegister = objectMapper.readValue(jsonUser, UserRegister.class);
            System.err.println(userRegister.email);

            // Check for empty fields
            if(userRegister.checkForEmptyFields(userRegister)) {
                System.out.println("EMPTY FIELDS FOUND");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("EMPTY_FIELDS");
            }

            // Check if email is already stored
            if (userService.emailExist(userRegister.email)) {
                System.out.println("EMAIL EXISTS");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("EMAIL_EXISTS");
            }

            // Check if the passwords are not matching
            if (!userRegister.passwordCheck(userRegister)) {
                System.out.println("PASSWORDS DO NOT MATCH");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("PASSWORDS_NOT_MATCHING");
            }

            String imageName;
            if (profileImage != null) {
                // Store profile image
                imageName = imageStorageService.storeImage(profileImage);
            } else {
                imageName = null;
            }

            // Store new user
            User user = userRegister.transformToUser(userRegister);
            user.setProfilePicture(imageName);
            userService.storeUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
