package com.linkdin.app.api;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetConnectRequestsController {
    @Autowired
    UserNetworkService userNetworkService;

    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getconnectrequests")
    public ResponseEntity<Object> connectRequests(UserIdentifiers userIdentifiers, HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            ListUsers pendingConnectResults = userNetworkService.getPendingRequests(Integer.parseInt(userIdentifiers.id));

            return new ResponseEntity<Object>(pendingConnectResults, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
