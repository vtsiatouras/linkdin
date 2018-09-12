package com.linkdin.app.repositories;

import com.linkdin.app.model.Notifications;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> getAllByUserIdOrderByTimestampDesc(Pageable pageable, Integer userID);
}
