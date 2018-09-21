package com.linkdin.app.repositories;

import com.linkdin.app.model.PostApplications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostApplicationsRepository extends JpaRepository<PostApplications, Long> {
    List<PostApplications> findAllByUserId(int userID);
    PostApplications findByPostIdAndUserId(int postID, int userID);
    List<PostApplications> findAllByPostId(int postID);
    int countAllByPostId(int postID);
}
