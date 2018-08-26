package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class PostCommentController {
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/postcomment")
    public ResponseEntity<Object> postComment(@RequestBody String jsonCommentData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonCommentData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject commentDataObj = obj.getJSONObject("commentData");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = commentDataObj.getString("postID");
            String comment = commentDataObj.getString("comment");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post != null) {
                int userIDPostOwner = post.getUserId();

                //TODO merge the below ifs, na to kanw kai stous allous controller
                // Check if the post belongs to the user that posted the comment
                if (userIDPostOwner == Integer.parseInt(userIdentifiers.id)) {
                    postCommentService.addComment(comment, Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                // Check if post belongs to connected user
                if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id))) {
                    postCommentService.addComment(comment, Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                    return new ResponseEntity<>(HttpStatus.OK);
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
