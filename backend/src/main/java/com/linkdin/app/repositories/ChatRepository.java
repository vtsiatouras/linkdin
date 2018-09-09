package com.linkdin.app.repositories;

import com.linkdin.app.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
     Chat findByUser1OrUser2AndId(Integer userID1, Integer userID2, Integer chatID);
     Chat findById(Integer chatID);
     List<Chat> findAllByUser1OrUser2AndIsActive(Integer userID1, Integer userID2, byte isActive);
}
