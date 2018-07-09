package com.linkdin.app.api;

import com.linkdin.app.dto.Credentials;
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
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpServletResponse response) {
        System.out.println(credentials.email + " " + credentials.password);
        if (userService.authenticate(credentials.email, credentials.password)) {
            response.addCookie(new Cookie("SessionLinkDIn", "6985072887")); //TODO
            return ResponseEntity.ok(credentials.email);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

