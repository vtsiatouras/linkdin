package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserBasicInfo;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.dto.UserInfo;
import com.linkdin.app.model.User;
import com.linkdin.app.services.*;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping(path = "/user")
    public ResponseEntity<Object> user(UserIdentifiers userIdentifiers, @RequestParam String profileUserID,
                                       HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Send requested user's profile info
            User user = userService.returnUserByID(Integer.parseInt(profileUserID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // If the user profile that requested belongs to admin
            // Return Unauthorized
            if (user.getIsAdmin() == 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String jsonUser = new JSONObject()
                    .put("firstName", user.getName())
                    .put("lastName", user.getSurname())
                    .toString();

            String image = imageStorageService.getImage(user.getProfilePicture());
            String responseObject = new JSONObject()
                    .put("user", jsonUser)
                    .put("profileImage", image)
                    .toString();

            return new ResponseEntity<Object>(responseObject, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/getuserbasicinfo")
    public ResponseEntity<Object> userBasicInfo(UserIdentifiers userIdentifiers, @RequestParam String userIdInfo,
                                                HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdInfo));
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

    @PostMapping(path = "/setuserinfo")
    public ResponseEntity<Object> setUserInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestUserInfo);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject userInfoRequestObj = obj.getJSONObject("userInfoUpdate");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            UserInfo userInfo = objectMapper.readValue(userInfoRequestObj.toString(), UserInfo.class);
            ;

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            user.setPhoneNumber(userInfo.phoneNumber);
            user.setPublicPhoneNumber((byte) userInfo.isPhonePublic);
            user.setCity(userInfo.city);
            user.setPublicCity((byte) userInfo.isCityPublic);
            user.setProfession(userInfo.profession);
            user.setPublicProfession((byte) userInfo.isProfessionPublic);
            user.setCompany(userInfo.company);
            user.setPublicCompany((byte) userInfo.isCompanyPublic);
            user.setEducation(userInfo.education);
            user.setPublicEducation((byte) userInfo.isEducationPublic);
            userService.storeUser(user);
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
