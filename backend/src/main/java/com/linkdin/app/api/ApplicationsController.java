package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linkdin.app.dto.InterestData;
import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.*;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class ApplicationsController {
    @Autowired
    PostApplicationService postApplicationService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    NotificationsService notificationsService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/apply")
    public ResponseEntity<Object> apply(@RequestBody String jsonApplyData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonApplyData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject applyObj = obj.getJSONObject("apply");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = applyObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Only ads posts has the apply feature
            if (post.getIsAdvertisment() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that clicked interest button OR
            // Check if post belongs to connected user
            if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    post.getIsPublic() == 1) {
                postApplicationService.addApply(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                // Don't push notification with comments/interests from the user that owns the post
                if (userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                    notificationsService.createNewNotification(Integer.parseInt(userIdentifiers.id), userIDPostOwner,
                                                               Integer.parseInt(postID), 3);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/applicationsdata")
    public ResponseEntity<Object> applicationsNumber(UserIdentifiers userIdentifiers, @RequestParam String postID,
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
