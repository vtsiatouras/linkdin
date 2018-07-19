package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ProfileRequest;
import com.linkdin.app.dto.UserAttributes;
import com.linkdin.app.model.User;
import com.linkdin.app.services.ImageStorageService;
import com.linkdin.app.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/user")
    public ResponseEntity<String> user(@RequestBody String jsonProfileRequest, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ProfileRequest profileRequest = objectMapper.readValue(jsonProfileRequest, ProfileRequest.class);
            // TODO authenticate user...

            // Send requested user's profile info
            User user = userService.returnUserByID(Integer.parseInt(profileRequest.profileUserID));
            String jsonString = new JSONObject()
                    .put("firstName", user.getName())
                    .put("lastName", user.getSurname())
                    .put("phoneNumber", user.getPhoneNumber())
                    .toString();
            return ResponseEntity.ok(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
