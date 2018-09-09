package com.linkdin.app.services;

import com.linkdin.app.model.Chat;
import com.linkdin.app.model.ChatHistory;
import com.linkdin.app.repositories.ChatHistoryRepository;
import com.linkdin.app.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    ChatHistoryRepository chatHistoryRepository;

    public void createNewChat(Integer userID1, Integer userID2) {
        Chat chat = new Chat();
        chat.setUser1(userID1);
        chat.setUser2(userID2);
        chat.setIsActive((byte) 0);
        chatRepository.save(chat);
    }

    public List getUserActiveChats(Integer userID) {
        return chatRepository.findAllByUser1OrUser2AndIsActive(userID, userID, (byte) 1);
    }

    public boolean checkChatWithUserID(Integer chatID, Integer userID) {
        if (chatRepository.findByUser1OrUser2AndId(userID, userID, chatID) == null) {
            return false;
        }
        return true;
    }

    public void sendMessage(Integer chatID, Integer senderID, String message) {
        Chat chat = chatRepository.findById(chatID);
        if (chat == null) {
            return;
        }
        if (chat.getIsActive() == 0) {
            chat.setIsActive((byte) 1);
            chatRepository.save(chat);
        }
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setChatId(chatID);
        chatHistory.setSenderUserId(senderID);
        chatHistory.setMessageContent(message);
        Date date = new java.util.Date();
        Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        chatHistory.setTimestamp(sqlDate);
        chatHistoryRepository.save(chatHistory);
    }

    public List<ChatHistory> getMessagesFromChat(Integer chatID) {
        return chatHistoryRepository.findAllByChatIdOrderByTimestamp(chatID);
    }
}
