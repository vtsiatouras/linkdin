package com.linkdin.app.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ProfileRequest;
import com.linkdin.app.dto.UserAttributes;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ImageStorageService;
import com.linkdin.app.services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/user")
    public ResponseEntity<Object> user(@RequestBody String jsonProfileRequest, HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject obj = new JSONObject(jsonProfileRequest);

        System.err.println(jsonProfileRequest);
        try {
            JSONObject userAttrs = obj.getJSONObject("userAttrs");
            JSONObject profileReq = obj.getJSONObject("requestProfile");
            ProfileRequest profileRequest = objectMapper.readValue(profileReq.toString(), ProfileRequest.class);
            UserAttributes userAttributes = objectMapper.readValue(userAttrs.toString(), UserAttributes.class);

            // TODO check if the requested profile is user's that send the request
            // TODO check if the requested profile is not user's friend

            // Authenticate user
            if (!authRequestService.authenticateRequest(userAttributes, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Send requested user's profile info
            User user = userService.returnUserByID(Integer.parseInt(profileRequest.profileUserID));
            String jsonUser = new JSONObject()
                    .put("firstName", user.getName())
                    .put("lastName", user.getSurname())
                    .put("phoneNumber", user.getPhoneNumber())
                    .toString();

            System.err.println(jsonUser);

            String imagePathName = "user_images/" + user.getProfilePicture();
            File userImg = new File(imagePathName);
            byte[] imageBytes = Files.readAllBytes(userImg.toPath());
            String base64String = Base64.encodeBase64String(imageBytes);
            String responseObject = new JSONObject()
                    .put("user", jsonUser)
                    .put("profileImage", base64String)
                    .toString();

            return new ResponseEntity<Object>(responseObject, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
