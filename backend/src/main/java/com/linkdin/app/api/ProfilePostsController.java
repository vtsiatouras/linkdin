package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AdminAuthRequestService;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostService;
import com.linkdin.app.services.UserNetworkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class ProfilePostsController {

    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    UserNetworkService userNetworkService;

    @GetMapping(path = "/getprofileposts")
    public ResponseEntity<Object> profilePosts(UserIdentifiers userIdentifiers, @RequestParam String profileUserID,
                                               @RequestParam String pageNumber, @RequestParam String limit,
                                               HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            int userID = Integer.parseInt(profileUserID);
            int _pageNumber_ = Integer.parseInt(pageNumber);
            int _limit_ = Integer.parseInt(limit);
            Page page;
            // If the requested profile belongs to the user that made the request
            // If they are connected
            // If admin requested
            if (profileUserID.equals(userIdentifiers.id) ||
                    userNetworkService
                            .checkIfConnected(Integer.parseInt(profileUserID), Integer.parseInt(userIdentifiers.id)) ||
                    isAdmin) {
                page = postService.getUserPosts(userID, _pageNumber_, _limit_);
                return new ResponseEntity<Object>(page, HttpStatus.OK);
            }
            // If not return only the public posts
            else {
                page = postService.getUsersPublicPosts(userID, _pageNumber_, _limit_);
            }
            return new ResponseEntity<Object>(page, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
