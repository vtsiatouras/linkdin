package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Chat;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GetChatByUsersIdsController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getchatbyusersids")
    public ResponseEntity<Object> getChatByUsersIds(UserIdentifiers userIdentifiers, @RequestParam String userID1,
                                                    @RequestParam String userID2, HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Chat chat = chatService.getChatIDByUsers(Integer.parseInt(userID1), Integer.parseInt(userID2));

            return new ResponseEntity<>(chat, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
