package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.dto.UserInfo;
import com.linkdin.app.model.User;
import com.linkdin.app.services.AuthRequestService;
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
public class StoreUserInfoController {

    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/setuserinfo")
    public ResponseEntity<Object> setUserInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestUserInfo);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject userInfoRequest = obj.getJSONObject("userInfoUpdate");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            UserInfo userInfo = objectMapper.readValue(userInfoRequest.toString(), UserInfo.class);;

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            //todo check if user exists

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
