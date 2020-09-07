package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ChatGetMessagesController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getchatmessages")
    public ResponseEntity<Object> getChatMessages(UserIdentifiers userIdentifiers, Integer chatID, HttpSession session) {

        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (!chatService.checkChatWithUserID(chatID, Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = chatService.getMessagesFromChat(chatID);
            System.out.print(list);
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

