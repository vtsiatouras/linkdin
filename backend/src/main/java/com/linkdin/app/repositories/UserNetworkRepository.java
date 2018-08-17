package com.linkdin.app.repositories;

import com.linkdin.app.model.UserNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNetworkRepository extends JpaRepository<UserNetwork, Long> {
    // Returns all connections of a user
    List<UserNetwork> findByUser1OrUser2AndIsAccepted(String user1, String user2, byte isAccepted);

    // Returns all the pending incoming connect requests
    List<UserNetwork> findByUser2AndIsAccepted(String user2, byte isAccepted);

    // Returns the connection between two users
    UserNetwork findByUser1AndUser2(String user1, String user2);

    // Returns the connection between two users
    UserNetwork findByUser1AndUser2AndIsAccepted(String user1, String user2, byte isAccepted);
}