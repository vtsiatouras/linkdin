package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.InterestData;
import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostInterestService;
import com.linkdin.app.services.PostService;
import com.linkdin.app.services.UserNetworkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetInterestedUsersController {
    @Autowired
    PostInterestService postInterestService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/interestedusersinfo")
    public ResponseEntity<Object> interestedUsers(@RequestBody String jsonInterestData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonInterestData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject interestedUsersObj = obj.getJSONObject("interestedUsers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = interestedUsersObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post != null) {
                int userIDPostOwner = post.getUserId();

                // Check if post belongs to connected user
                // or if the post belongs to the user that clicked interest button
                if (userNetworkService.checkIfConnected(Integer.toString(userIDPostOwner), userIdentifiers.id) ||
                        userIDPostOwner == Integer.parseInt(userIdentifiers.id)) {
                    ListUsers users = postInterestService.getInterestedUsersInfo(Integer.parseInt(postID));
                    return new ResponseEntity<>(users, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
