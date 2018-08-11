package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class AuthCheckController {

    @PostMapping(path = "/authcheck")
    public ResponseEntity<String> authCheck(@RequestBody UserIdentifiers userIdentifiers, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userIdentifiers.userToken == null || userIdentifiers.id == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).equals(userIdentifiers.id) &&
                session.getAttribute("userToken").equals(userIdentifiers.userToken)) {
            System.err.println("user is authenticated!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.err.println("user is NOT authenticated!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
