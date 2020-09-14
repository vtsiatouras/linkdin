package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ConnectionAttributes;
import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.User;
import com.linkdin.app.model.UserNetwork;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import com.linkdin.app.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    userNetworkService
                            .checkIfConnected(Integer.parseInt(profileUserID), Integer.parseInt(userIdentifiers.id))) {
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

    @GetMapping(path = "/getconnectrequests")
    public ResponseEntity<Object> connectRequests(UserIdentifiers userIdentifiers, HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            ListUsers pendingConnectResults =
                    userNetworkService.getPendingRequests(Integer.parseInt(userIdentifiers.id));

            return new ResponseEntity<Object>(pendingConnectResults, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/sendconnect")
    public ResponseEntity<Object> sendConnect(@RequestBody String jsonConnectRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonConnectRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject friendRequestObj = obj.getJSONObject("friendRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userRequestID = friendRequestObj.getString("userRequestID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userRequestID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            userNetworkService
                    .sendConnectRequest(Integer.parseInt(userIdentifiers.id), Integer.parseInt(userRequestID));
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/handleconnectrequest")
    public ResponseEntity<Object> handleConnect(@RequestBody String jsonConnectRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonConnectRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject connectRequestObj = obj.getJSONObject("connectRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userTargetProfileID = connectRequestObj.getString("profileUserID");
            String accepted = connectRequestObj.getString("accepted");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userTargetProfileID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (accepted.equals("1")) {
                userNetworkService.acceptConnectRequest(Integer.parseInt(userTargetProfileID),
                                                        Integer.parseInt(userIdentifiers.id));
            } else {
                userNetworkService.declineConnectRequest(Integer.parseInt(userTargetProfileID),
                                                         Integer.parseInt(userIdentifiers.id));
            }

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
