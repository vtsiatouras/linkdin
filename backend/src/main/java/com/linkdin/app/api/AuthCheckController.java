package com.linkdin.app.api;

import com.linkdin.app.dto.Credentials;
import com.linkdin.app.dto.UserToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class AuthCheckController {

    @PostMapping(path = "/authcheck")
    public ResponseEntity<String> authCheck(@RequestBody UserToken userToken, HttpSession session) {

        if(session == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(userToken == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).equals(userToken.userTok)){
            System.err.println("user is authenticated!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.err.println("user is NOT authenticated!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
