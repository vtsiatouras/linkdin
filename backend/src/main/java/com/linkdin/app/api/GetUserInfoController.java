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
public class GetUserInfoController {

    @Autowired
    UserService userService;
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

            UserInfo userInfo = new UserInfo();
            User user = userService.returnUserByID(Integer.parseInt(userId));
            userInfo.phoneNumber = user.getPhoneNumber();
            userInfo.isPhonePublic = user.getPublicPhoneNumber();
            userInfo.city = user.getCity();
            userInfo.isCityPublic = user.getPublicCity();
            userInfo.profession = user.getProfession();
            userInfo.isProfessionPublic = user.getPublicProfession();
            userInfo.company = user.getCompany();
            userInfo.isCompanyPublic = user.getPublicCompany();
            userInfo.education = user.getEducation();
            userInfo.isEducationPublic = user.getPublicEducation();

            return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
