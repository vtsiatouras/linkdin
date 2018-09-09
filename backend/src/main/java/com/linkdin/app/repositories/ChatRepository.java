package com.linkdin.app.repositories;

import com.linkdin.app.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<PostComment, Long> {

}
