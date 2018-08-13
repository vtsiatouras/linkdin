package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ViewPostController {

    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getpost")
    public ResponseEntity<Object> userBasicInfo(@RequestBody String jsonRequestPost, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestPost);
        System.err.println(obj);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject postRequest = obj.getJSONObject("postRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = postRequest.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // TODO!!
            // In case that the post is private, check if the user that requested the post
            // is friend with the post's author
            // If the post is public just send it
            // TODO CHECK IN OTHER CONTROLLERS FOR NULL RETURNS!!!
            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post != null) {
                return new ResponseEntity<Object>(post, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
