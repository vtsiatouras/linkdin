package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.dto.UserInfo;
import com.linkdin.app.model.User;
import com.linkdin.app.model.UserNetwork;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import com.linkdin.app.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetUserInfoController {

    @Autowired
    UserService userService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getuserinfo")
    public ResponseEntity<Object> userInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestUserInfo);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject userInfoRequest = obj.getJSONObject("userInfoRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userId = userInfoRequest.getString("userIdInfo");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            //todo check if user exists

            // If the requested network belongs to the user that made the request
            if (userId.equals(userIdentifiers.id)) {
                UserInfo userInfo = userService.getUserInfo(userId);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
            // If the network belongs to a friend
            if (userNetworkService.checkIfConnected(userId, userIdentifiers.id)) {
                UserInfo userInfo = userService.getUserInfo(userId);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
            // If not return public info
            else {
                UserInfo userInfo = userService.getPublicUserInfo(userId);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
