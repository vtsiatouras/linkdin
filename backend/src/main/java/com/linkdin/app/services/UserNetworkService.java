package com.linkdin.app.services;

import com.linkdin.app.model.UserNetwork;
import com.linkdin.app.repositories.UserNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserNetworkService {

    @Autowired
    UserNetworkRepository userNetworkRepository;

    public List getFriends(String userID) {
        List<UserNetwork> allConnections = userNetworkRepository.findByUser1OrUser2AndIsAccepted(userID, userID, (byte) 1);
        ArrayList<String> resultList = new ArrayList<String>();
        for (UserNetwork element : allConnections) {
            if (!element.getUser1().equals(userID)) {
                resultList.add(element.getUser2());
            } else {
                resultList.add(element.getUser1());
            }
        }
        return resultList;
    }

    public UserNetwork returnConnection(String userID1, String userID2) {
        return userNetworkRepository.findByUser1AndUser2(userID1, userID2);
    }

    public void sendFriendRequest(String senderID, String receiverID) {
        UserNetwork newConnection = new UserNetwork();
        newConnection.setUser1(senderID);
        newConnection.setUser2(receiverID);
        newConnection.setIsAccepted((byte) 0);
        userNetworkRepository.save(newConnection);
    }

    public boolean acceptFriendRequest(String senderID, String receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            connection.setIsAccepted((byte) 1);
            return true;
        } else {
            return false;
        }
    }

    public boolean declineFriendRequest(String senderID, String receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            userNetworkRepository.delete(connection);
            return true;
        } else {
            return false;
        }
    }

}
