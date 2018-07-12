package com.linkdin.app.api;

import com.linkdin.app.dto.Credentials;
import com.linkdin.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

//    @Autowired
//    private HttpSession session;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpSession session) {
        System.out.println(credentials.email + " " + credentials.password);
        if (userService.authenticate(credentials.email, credentials.password)) {
            String email = credentials.email;

            session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, email);
//            session.setAttribute("email", email);

            return ResponseEntity.ok(credentials.email);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}

