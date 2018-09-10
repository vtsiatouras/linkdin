package com.linkdin.app.repositories;

import com.linkdin.app.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByUser1AndIdOrUser2AndId(Integer userID1, Integer chatID1, Integer userID2, Integer chatID2);

    Chat findById(Integer chatID);

    List<Chat> findAllByUser1AndIsActiveOrUser2AndIsActive(Integer userID1, byte isActive1, Integer userID2, byte isActive2);
}
