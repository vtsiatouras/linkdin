package com.linkdin.app.api;

import com.linkdin.app.dto.InterestData;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetApplicationsDataController {
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

    @GetMapping(path = "/applicationsdata")
    public ResponseEntity<Object> applicationsNumber(UserIdentifiers userIdentifiers, String postID,
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

            // Only ads posts have the apply feature
            if (post.getIsAdvertisment() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that owns the ad
            // or requested by admin
            InterestData interestData = new InterestData();
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) || isAdmin) {
                interestData.numberOfInterestedUsers =
                        postApplicationService.getApplicationsNumber(Integer.parseInt(postID));
                interestData.isUserInterested = postApplicationService
                        .checkIfApplied(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                return new ResponseEntity<>(interestData, HttpStatus.OK);
            }
            // If is requested by other user return only if this user is already applied
            else if (userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                interestData.numberOfInterestedUsers = 0;
                interestData.isUserInterested = postApplicationService
                        .checkIfApplied(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
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
