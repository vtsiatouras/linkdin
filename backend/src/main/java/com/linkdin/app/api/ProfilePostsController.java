package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.ProfilePostsPageRequest;
import com.linkdin.app.services.PostService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ProfilePostsController {

    @Autowired
    PostService postService;

    // TODO authCheck
    // TODO later check if the profile belongs to friend to show all posts
    // if it is not show only public posts
    @PostMapping(path = "/getprofileposts")
    public ResponseEntity<Object> newPost(@RequestBody String jsonPostsRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonPostsRequest);
        try {
            JSONObject pageRequest = obj.getJSONObject("pageRequest");
            ProfilePostsPageRequest profilePostsPageRequest = objectMapper.readValue(pageRequest.toString(), ProfilePostsPageRequest.class);
            int userID = Integer.parseInt(profilePostsPageRequest.profileUserID);
            int pageNumber = Integer.parseInt(profilePostsPageRequest.pageNumber);
            int limit = Integer.parseInt(profilePostsPageRequest.limit);
            Page page = postService.getUserPosts(userID, pageNumber, limit);
            return new ResponseEntity<Object>(page, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
