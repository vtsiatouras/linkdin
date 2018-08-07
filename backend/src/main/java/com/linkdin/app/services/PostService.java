package com.linkdin.app.services;

import com.linkdin.app.dto.NewPostData;
import com.linkdin.app.model.Post;
import com.linkdin.app.model.User;
import com.linkdin.app.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public boolean createPost(NewPostData newPostData, User user) {
        // TODO set public flag to database
        Post post = new Post();
        post.setContent(newPostData.postContent);
        if (newPostData.isAd) {
            post.setIsAdvertisment((byte) 1);
        } else {
            post.setIsAdvertisment((byte) 0);
        }
        if (newPostData.isPublic) {
            post.setIsPublic((byte) 1);
        } else {
            post.setIsPublic((byte) 0);
        }
        Date date = new java.util.Date();
        Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        post.setTimestamp(sqlDate);
        post.setUserId(user.getId());
        postRepository.save(post);
        System.err.println("post saved!");
        return true;
    }


    public Page<Post> getUserPosts(int userID, int pageNumber, int limit) {
//        return postRepository.findAll(new PageRequest(pageNumber, limit));
        return postRepository.findByuserId(new PageRequest(pageNumber, limit), userID);
    }

    // WIP
    public List getAllUsersPosts() {
        return null;
    }

    public List getAllUsersPublicPosts() {
        return null;
    }

    public List getNetworkPosts() {
        return null;
    }

}
