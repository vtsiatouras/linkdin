package com.linkdin.app.api;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetInterestedUsersController {
    @Autowired
    PostInterestService postInterestService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @GetMapping(path = "/interestedusersinfo")
    public ResponseEntity<Object> interestedUsers(UserIdentifiers userIdentifiers, @RequestParam String postID,
                                                  HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            int userIDPostOwner = post.getUserId();

            // Check if post belongs to connected user
            // or if the post belongs to the user that clicked interest button
            // or requested by admin
            if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    post.getIsPublic() == 1 ||
                    isAdmin) {
                ListUsers users = postInterestService.getInterestedUsersInfo(Integer.parseInt(postID));
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
