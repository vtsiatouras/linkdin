package com.linkdin.app.api;

import java.util.HashMap;
import java.util.Map;

import com.linkdin.app.dto.Credentials;
import com.linkdin.app.dto.UserDTO;
import com.linkdin.app.model.User;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginController {

    @Autowired
    UserService userService;
    @PostMapping(path = "/login")
    public HttpServletResponse login(@RequestBody Credentials credentials, HttpServletResponse response) {
        if (userService.authenticate(credentials.email, credentials.password)) {
            response.addCookie(new Cookie("SessionLinkDIn", "6985072887"));
        }
        System.out.println(credentials.email + " " + credentials.password);
        return response;
//        return ResponseEntity.ok(credentials.email, response);
    }
}

