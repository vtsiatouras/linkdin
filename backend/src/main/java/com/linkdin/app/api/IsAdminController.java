package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class IsAdminController {
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/isadmin")
    public ResponseEntity<String> isAdmin(UserIdentifiers userIdentifiers, HttpSession session) {
        try {
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            if (user.getIsAdmin() == 1) {
                return new ResponseEntity<>("true", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("false", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
