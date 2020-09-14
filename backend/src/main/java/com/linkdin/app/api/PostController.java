package com.linkdin.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkdin.app.dto.NewPostData;
import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Post;
import com.linkdin.app.model.User;
import com.linkdin.app.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class PostController {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    UserNetworkService userNetworkService;

    @PostMapping(path = "/newpost")
    public ResponseEntity<Object> newPost(@RequestBody String jsonNewPost, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonNewPost);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject postObj = obj.getJSONObject("postData");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            NewPostData newPostData = objectMapper.readValue(postObj.toString(), NewPostData.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            postService.createPost(newPostData, user, "");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/postimage")
    public ResponseEntity<String> newPostImage(@RequestPart("userIdentifiers") String jsonUserIdentifiers,
                                               @RequestPart("postData") String jsonPostData,
                                               @RequestPart("image") MultipartFile image,
                                               HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserIdentifiers userIdentifiers = objectMapper.readValue(jsonUserIdentifiers, UserIdentifiers.class);
            JSONObject postObj = new JSONObject(jsonPostData);
            NewPostData newPostData = objectMapper.readValue(postObj.toString(), NewPostData.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String imageName;
            if (image != null) {
                // Store profile image
                imageName = imageStorageService.storeImage(image);
            } else {
                imageName = null;
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            postService.createPost(newPostData, user, imageName);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/getprofileposts")
    public ResponseEntity<Object> profilePosts(UserIdentifiers userIdentifiers, @RequestParam String profileUserID,
                                               @RequestParam String pageNumber, @RequestParam String limit,
                                               HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            int userID = Integer.parseInt(profileUserID);
            int _pageNumber_ = Integer.parseInt(pageNumber);
            int _limit_ = Integer.parseInt(limit);
            Page page;
            // If the requested profile belongs to the user that made the request
            // If they are connected
            // If admin requested
            if (profileUserID.equals(userIdentifiers.id) ||
                    userNetworkService
                            .checkIfConnected(Integer.parseInt(profileUserID), Integer.parseInt(userIdentifiers.id)) ||
                    isAdmin) {
                page = postService.getUserPosts(userID, _pageNumber_, _limit_);
                return new ResponseEntity<Object>(page, HttpStatus.OK);
            }
            // If not return only the public posts
            else {
                page = postService.getUsersPublicPosts(userID, _pageNumber_, _limit_);
            }
            return new ResponseEntity<Object>(page, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getpost")
    public ResponseEntity<Object> viewPost(UserIdentifiers userIdentifiers, @RequestParam String postID,
                                           HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // If post is public OR
            // If the requested profile belongs to the user that made the request OR
            // If they are connected
            if (post.getIsPublic() == 1 ||
                    (Integer.toString(post.getUserId())).equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(post.getUserId(), Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<Object>(post, HttpStatus.OK);
            }
            // If not return error
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
