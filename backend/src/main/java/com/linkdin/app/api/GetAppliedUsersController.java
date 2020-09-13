package com.linkdin.app.api;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetAppliedUsersController {

    @Autowired
    PostApplicationService postApplicationService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @GetMapping(path = "/appliedusersinfo")
    public ResponseEntity<Object> appliedUsers(UserIdentifiers userIdentifiers, @RequestParam String postID,
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

            // Only ads posts has the apply feature
            if (post.getIsAdvertisment() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the post owner
            // or requested by admin
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) || post.getIsPublic() == 1 || isAdmin) {
                ListUsers users = postApplicationService.getAppliedUsersInfo(Integer.parseInt(postID));
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
