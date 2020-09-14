package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ConnectionAttributes;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.User;
import com.linkdin.app.model.UserNetwork;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ConnectController {

    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/connectstatus")
    public ResponseEntity<Object> connectStatus(UserIdentifiers userIdentifiers, @RequestParam String profileUserID,
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

            UserNetwork userNetwork = userNetworkService.returnConnection(Integer.parseInt(userIdentifiers.id),
                    Integer.parseInt(profileUserID));
            ConnectionAttributes connectionAttributes = new ConnectionAttributes();

            // If they are not friends
            if (userNetwork == null) {
                connectionAttributes.friends = "0";
                connectionAttributes.pending = "0";
            } else {
                connectionAttributes.friends = "1";
                // If they are friends
                if (userNetwork.getIsAccepted() == 1) {
                    connectionAttributes.pending = "0";
                }
                // If there is pending request
                else {
                    connectionAttributes.pending = "1";
                }
            }
            return new ResponseEntity<Object>(connectionAttributes, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
