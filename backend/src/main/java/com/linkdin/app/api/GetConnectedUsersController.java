package com.linkdin.app.api;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import com.linkdin.app.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetConnectedUsersController {
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getconnectedusers")
    public ResponseEntity<Object> connectedUsers(UserIdentifiers userIdentifiers, @RequestParam String profileUserID,
                                                 HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(profileUserID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested network belongs to the user that made the request OR
            // If the network belongs to a friend
            if (profileUserID.equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(Integer.parseInt(profileUserID), Integer.parseInt(userIdentifiers.id))) {
                ListUsers connectedUsers = userNetworkService.getConnectedUsers(Integer.parseInt(profileUserID));
                return new ResponseEntity<Object>(connectedUsers, HttpStatus.OK);
            }
            // If not return error
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
