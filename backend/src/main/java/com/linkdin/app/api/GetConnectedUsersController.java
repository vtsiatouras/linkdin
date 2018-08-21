package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetConnectedUsersController {
    @Autowired
    UserNetworkService userNetworkService;

    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getconnectedusers")
    public ResponseEntity<Object> GetConnectedUsers(@RequestBody String jsonGetRequests, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonGetRequests);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject profileNetwork = obj.getJSONObject("profileNetwork");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userTargetProfileID = profileNetwork.getString("profileUserID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested network belongs to the user that made the request
            if (userTargetProfileID.equals(userIdentifiers.id)) {
                ListUsers connectedUsers = userNetworkService.getConnectedUsers(userTargetProfileID);
                return new ResponseEntity<Object>(connectedUsers, HttpStatus.OK);
            }
            // If the network belongs to a friend
            if (userNetworkService.checkIfConnected(userTargetProfileID, userIdentifiers.id)) {
                ListUsers connectedUsers = userNetworkService.getConnectedUsers(userTargetProfileID);
                return new ResponseEntity<Object>(connectedUsers, HttpStatus.OK);
            }
            // If not return error
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
