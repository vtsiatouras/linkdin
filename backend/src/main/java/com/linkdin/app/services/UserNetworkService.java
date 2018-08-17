package com.linkdin.app.services;

import com.linkdin.app.dto.ListUsers;
import com.linkdin.app.dto.UserBasicInfo;
import com.linkdin.app.model.User;
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
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

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

    public ListUsers getPendingRequests(String userID) {
        List<UserNetwork> pendingConnections = userNetworkRepository.findByUser2AndIsAccepted(userID, (byte) 0);
        ArrayList<UserBasicInfo> resultList = new ArrayList<UserBasicInfo>();
        for (UserNetwork element : pendingConnections) {
            String targetUserID;
            targetUserID = element.getUser1();
            User targetUser = userService.returnUserByID(Integer.parseInt(targetUserID));
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = targetUserID;
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            resultList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(resultList.size());
        ListUsers results = new ListUsers();
        results.list = resultList;
        results.numberOfResults = totalResults;
        return results;
    }

    public UserNetwork returnConnection(String userID1, String userID2) {
        UserNetwork userNetwork = userNetworkRepository.findByUser1AndUser2(userID1, userID2);
        if(userNetwork != null) {
            return userNetwork;
        }
        userNetwork = userNetworkRepository.findByUser1AndUser2(userID2, userID1);
        return userNetwork;
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
            userNetworkRepository.save(connection);
            System.err.println("HERE");
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
