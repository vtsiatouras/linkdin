package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.dto.UserInfo;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AdminAuthRequestService;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserNetworkService;
import com.linkdin.app.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetUserInfoController {

    @Autowired
    UserService userService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;

    @GetMapping(path = "/getuserinfo")
    public ResponseEntity<Object> userInfo(UserIdentifiers userIdentifiers, @RequestParam String userIdInfo,
                                           HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            User user = userService.returnUserByID(Integer.parseInt(userIdInfo));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested profile belongs to the user that made the request OR
            // If the requested info belongs to a friend
            // If admin requested
            if (userIdInfo.equals(userIdentifiers.id) || userNetworkService
                    .checkIfConnected(Integer.parseInt(userIdInfo), Integer.parseInt(userIdentifiers.id)) || isAdmin) {
                UserInfo userInfo = userService.getUserInfo(userIdInfo);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
            // If not return public info
            else {
                UserInfo userInfo = userService.getPublicUserInfo(userIdInfo);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
