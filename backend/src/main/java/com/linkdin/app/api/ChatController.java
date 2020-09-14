package com.linkdin.app.api;

import com.linkdin.app.dto.UserIdentifiers;
import com.linkdin.app.model.Chat;
import com.linkdin.app.services.AuthRequestService;
import com.linkdin.app.services.ChatService;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @GetMapping(path = "/getactivechats")
    public ResponseEntity<Object> getActiveChats(UserIdentifiers userIdentifiers, HttpSession session) {
        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = chatService.getUserActiveChats(Integer.parseInt(userIdentifiers.id));

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getchatmessages")
    public ResponseEntity<Object> getChatMessages(UserIdentifiers userIdentifiers, @RequestParam String chatID,
                                                  HttpSession session) {

        try {
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (!chatService.checkChatWithUserID(Integer.parseInt(chatID), Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = chatService.getMessagesFromChat(Integer.parseInt(chatID));
            System.out.print(list);
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/sendchatmessage")
    public ResponseEntity<Object> sendChatMessage(@RequestBody String jsonChatRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonChatRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject chatMessageObj = obj.getJSONObject("chatMessageContent");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Integer chatID = chatMessageObj.getInt("chatID");
            String message = chatMessageObj.getString("messageContent");
            String senderID = userIdentifiers.id;

            if (!chatService.checkChatWithUserID(chatID, Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            chatService.sendMessage(chatID, Integer.parseInt(senderID), message);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
