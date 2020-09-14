package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AdminAuthRequestService;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;

    @GetMapping(path = "/adminlistusers")
    public ResponseEntity<Object> listAllUsers(UserIdentifiers adminIdentifiers, HttpSession session) {
        try {
            // Authenticate request
            if (!adminAuthRequestService.authenticateRequest(adminIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = userService.listAllUsers();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
