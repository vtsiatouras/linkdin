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
            JSONObject userInfoRequestObj = obj.getJSONObject("userInfoRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userId = userInfoRequestObj.getString("userIdInfo");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userId));
            if(user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested profile belongs to the user that made the request OR
            // If the requested info belongs to a friend
            if (userId.equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(Integer.parseInt(userId), Integer.parseInt(userIdentifiers.id))) {
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
