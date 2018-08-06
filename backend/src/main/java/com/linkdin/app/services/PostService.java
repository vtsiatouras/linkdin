package com.linkdin.app.services;

import com.linkdin.app.dto.NewPostData;
import com.linkdin.app.model.Post;
import com.linkdin.app.model.User;
import com.linkdin.app.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        post.setUserId(user.getId());
        postRepository.save(post);
        System.err.println("post saved!");
        return true;
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
