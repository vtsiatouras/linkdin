package com.linkdin.app.repositories;

import com.linkdin.app.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByuserIdOrderByTimestampDesc(Pageable pageable, Integer userID);
    Page<Post> findByuserIdAndIsPublicOrderByTimestampDesc(Pageable pageable, Integer userID, byte isPublic);
    Post findById(Integer postID);
}
