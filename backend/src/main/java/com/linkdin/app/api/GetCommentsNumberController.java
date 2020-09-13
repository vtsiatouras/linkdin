package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetCommentsNumberController {
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @GetMapping(path = "/gettotalcomments")
    public ResponseEntity<Object> getTotalComments(UserIdentifiers userIdentifiers,
                                                   @RequestParam String postID, HttpSession session) {
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

            // Check if post belongs to connected user OR
            // Check if the post belongs to the user that posted the comment
            // Or requested by admin
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    post.getIsPublic() == 1 ||
                    isAdmin) {
                int commentsNumber = postCommentService.getCommentsNumber(Integer.parseInt(postID));
                return new ResponseEntity<>(commentsNumber, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
