package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostService;
import com.linkdin.app.services.UserNetworkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetPostsFromFriendsController {

    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getpostsfromfriends")
    public ResponseEntity<Object> postsFromFriends(UserIdentifiers userIdentifiers, @RequestParam String pageNumber,
                                                   @RequestParam String limit, HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Send requested user's profile info
            List friendList = userNetworkService.getConnectedUsersIDsOnly(Integer.parseInt(userIdentifiers.id));

            // If user has friends send all friendly posts
            if (friendList.size() > 0) {
                Page list = postService.getNetworkPostsAndFriendInterestPosts(friendList,
                                                                              Integer.parseInt(userIdentifiers.id),
                                                                              Integer.parseInt(pageNumber),
                                                                              Integer.parseInt(limit));
                return new ResponseEntity<Object>(list, HttpStatus.OK);
            }
            // Else send only his own posts
            else {
                Page list = postService.getUserPosts(Integer.parseInt(userIdentifiers.id),
                                                     Integer.parseInt(pageNumber), Integer.parseInt(limit));
                return new ResponseEntity<Object>(list, HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
