package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SendConnectRequestController {

    @Autowired
    UserNetworkService userNetworkService;

    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/sendconnect")
    public ResponseEntity<Object> SendConnect(@RequestBody String jsonConnectRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonConnectRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject friendRequest = obj.getJSONObject("friendRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userRequestID = friendRequest.getString("userRequestID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            userNetworkService.sendFriendRequest(userIdentifiers.id, userRequestID);

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
