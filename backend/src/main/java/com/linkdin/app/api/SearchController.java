package com.linkdin.app.api;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class SearchController {

    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/searchusers")
    public ResponseEntity<Object> search(UserIdentifiers userIdentifiers, @RequestParam String searchQuery,
                                         HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            ListUsers result = userService.searchUsers(searchQuery);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}