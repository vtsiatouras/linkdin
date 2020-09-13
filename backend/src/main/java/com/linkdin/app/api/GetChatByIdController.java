package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Chat;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetChatByIdController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getchatbyid")
    public ResponseEntity<Object> getChatID(UserIdentifiers userIdentifiers, @RequestParam String chatID,
                                            HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (!chatService.checkChatWithUserID(Integer.parseInt(chatID), Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Chat chat = chatService.getChatByID(Integer.parseInt(chatID));

            return new ResponseEntity<>(chat, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
