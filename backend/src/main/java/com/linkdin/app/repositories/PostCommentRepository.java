package com.linkdin.app.repositories;

import com.linkdin.app.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    //    PostComment findByPostIdAndUserId(int postID, int userID);
    List<PostComment> findAllByPostIdOrderByCommentTimestamp(int postID);
    List<PostComment> findByUserId(int userID);
    int countAllByPostId(int PostID);
}
