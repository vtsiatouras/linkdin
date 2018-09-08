package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostService;
import com.linkdin.app.services.UserNetworkService;
import jdk.nashorn.internal.ir.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ExportUsersToXMLController {
    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/exportusers")
    public ResponseEntity<Object> exportUsers(@RequestBody String jsonUsers, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonUsers);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject listObj = obj.getJSONObject("userListRequest");

            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode node = (ObjectNode)mapper.readTree(listObj.toString());
//
//            List userIDs = listObj.
            JSONArray userList = listObj.getJSONArray("usersToExport");
            for (int i = 0; i < userList.length(); i++) {
                String userID = userList.get(i).toString();
                System.err.println(userID);
            }
//            for(JSONObject element : userList)
//            {
//                //do something with your cargo element
//            }

//            System.err.println(userList);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
