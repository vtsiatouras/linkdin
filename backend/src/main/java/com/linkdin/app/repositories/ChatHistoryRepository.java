package com.linkdin.app.repositories;

import com.linkdin.app.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findAllByChatIdOrderByTimestamp(Integer chatID);

}
