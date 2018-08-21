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

    public ListUsers getConnectedUsers(String userID) {
        List<UserNetwork> allConnections = userNetworkRepository.findByUser1AndIsAcceptedOrUser2AndIsAccepted(userID, (byte) 1, userID, (byte) 1);
        ArrayList<UserBasicInfo> tempList = new ArrayList<UserBasicInfo>();
        for (UserNetwork element : allConnections) {
            String targetUserID;
            if (element.getUser1().equals(userID)) {
                targetUserID = element.getUser2();
            } else {
                targetUserID = element.getUser1();
            }
            User targetUser = userService.returnUserByID(Integer.parseInt(targetUserID));
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = targetUserID;
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            tempList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(tempList.size());
        ListUsers results = new ListUsers();
        results.list = tempList;
        results.numberOfResults = totalResults;
        return results;
    }

    public List getConnectedUsersIDsOnly(String userID) {
        List<UserNetwork> allConnections = userNetworkRepository.findByUser1AndIsAcceptedOrUser2AndIsAccepted(userID, (byte) 1, userID, (byte) 1);
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (UserNetwork element : allConnections) {
            if (element.getUser1().equals(userID)) {
                resultList.add(Integer.parseInt(element.getUser2()));
            } else {
                resultList.add(Integer.parseInt(element.getUser1()));
            }
        }
        resultList.add(Integer.parseInt(userID));
        return resultList;
    }

    public ListUsers getPendingRequests(String userID) {
        List<UserNetwork> pendingConnections = userNetworkRepository.findByUser2AndIsAccepted(userID, (byte) 0);
        ArrayList<UserBasicInfo> tempList = new ArrayList<UserBasicInfo>();
        for (UserNetwork element : pendingConnections) {
            String targetUserID;
            targetUserID = element.getUser1();
            User targetUser = userService.returnUserByID(Integer.parseInt(targetUserID));
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = targetUserID;
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            tempList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(tempList.size());
        ListUsers results = new ListUsers();
        results.list = tempList;
        results.numberOfResults = totalResults;
        return results;
    }

    public boolean checkIfConnected(String userID1, String userID2) {
        UserNetwork userNetwork = userNetworkRepository.findByUser1AndUser2AndIsAccepted(userID1, userID2, (byte) 1);
        if (userNetwork != null) {
            return true;
        }
        userNetwork = userNetworkRepository.findByUser1AndUser2AndIsAccepted(userID2, userID1, (byte) 1);
        if (userNetwork != null) {
            return true;
        }
        return false;
    }

    public UserNetwork returnConnection(String userID1, String userID2) {
        UserNetwork userNetwork = userNetworkRepository.findByUser1AndUser2(userID1, userID2);
        if (userNetwork != null) {
            return userNetwork;
        }
        userNetwork = userNetworkRepository.findByUser1AndUser2(userID2, userID1);
        return userNetwork;
    }

    public void sendConnectRequest(String senderID, String receiverID) {
        UserNetwork newConnection = new UserNetwork();
        newConnection.setUser1(senderID);
        newConnection.setUser2(receiverID);
        newConnection.setIsAccepted((byte) 0);
        userNetworkRepository.save(newConnection);
    }

    public boolean acceptConnectRequest(String senderID, String receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            connection.setIsAccepted((byte) 1);
            userNetworkRepository.save(connection);
            return true;
        } else {
            return false;
        }
    }

    public boolean declineConnectRequest(String senderID, String receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            userNetworkRepository.delete(connection);
            return true;
        } else {
            return false;
        }
    }

}
