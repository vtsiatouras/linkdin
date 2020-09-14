package com.linkdin.app.api;

import com.linkdin.app.dto.InterestData;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetInterestsDataController {

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

    @GetMapping(path = "/interestsdata")
    public ResponseEntity<Object> interestsNumber(UserIdentifiers userIdentifiers, @RequestParam String postID,
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
            // or if the post belongs to the post owner
            // or requested by admin
            if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    post.getIsPublic() == 1 ||
                    isAdmin) {
                InterestData interestData = new InterestData();
                interestData.numberOfInterestedUsers = postInterestService.getInterestsNumber(Integer.parseInt(postID));
                interestData.isUserInterested = postInterestService.checkIfInterested(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                return new ResponseEntity<>(interestData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
