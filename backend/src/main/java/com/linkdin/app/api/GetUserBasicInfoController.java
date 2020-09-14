package com.linkdin.app.api;

import com.linkdin.app.dto.UserBasicInfo;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ImageStorageService;
import com.linkdin.app.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetUserBasicInfoController {

    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getuserbasicinfo")
    public ResponseEntity<Object> userBasicInfo(UserIdentifiers userIdentifiers, @RequestParam String userId,
                                                HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userId));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            UserBasicInfo userBasicInfo = new UserBasicInfo();
            userBasicInfo.id = Integer.toString(user.getId());
            userBasicInfo.name = user.getName();
            userBasicInfo.surname = user.getSurname();
            userBasicInfo.image = imageStorageService.getImage(user.getProfilePicture());
            return new ResponseEntity<Object>(userBasicInfo, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
