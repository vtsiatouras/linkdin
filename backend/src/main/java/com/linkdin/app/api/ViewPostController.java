package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.PostService;
import com.linkdin.app.services.UserNetworkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ViewPostController {

    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getpost")
    public ResponseEntity<Object> viewPost(@RequestBody String jsonRequestPost, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestPost);
        System.err.println(obj);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject postRequestObj = obj.getJSONObject("postRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = postRequestObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post != null) {
                // If post is public just send it
                if(post.getIsPublic() == 1) {
                    return new ResponseEntity<Object>(post, HttpStatus.OK);
                }
                // If the requested profile belongs to the user that made the request
                if ((Integer.toString(post.getUserId())).equals(userIdentifiers.id)) {
                    return new ResponseEntity<Object>(post, HttpStatus.OK);
                }
                // If they are connected
                if (userNetworkService.checkIfConnected(post.getUserId(), Integer.parseInt(userIdentifiers.id))) {
                    return new ResponseEntity<Object>(post, HttpStatus.OK);
                }
                // If not return error
                else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // TODO CHECK IN OTHER CONTROLLERS FOR NULL RETURNS!!!
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
