package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.SearchAttributes;
import com.linkdin.app.services.UserService;
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
public class SearchController {

    @Autowired
    UserService userService;

    // TODO run authcheck
    @PostMapping(path = "/searchusers")
    public ResponseEntity<Object> user(@RequestBody String jsonSearchRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonSearchRequest);
        try {
            JSONObject searchObj = obj.getJSONObject("searchData");
            SearchAttributes searchAttributes = objectMapper.readValue(searchObj.toString(), SearchAttributes.class);
            List results = userService.searchUsers(searchAttributes.searchQuery);
            System.err.println(results);
            return new ResponseEntity<Object>(results, HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}