package com.linkdin.app.services;

import com.linkdin.app.api.HandleConnectRequestController;
import com.linkdin.app.dto.HomePagePost;
import com.linkdin.app.dto.NewPostData;
import com.linkdin.app.model.Post;
import com.linkdin.app.model.User;
import com.linkdin.app.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public boolean createPost(NewPostData newPostData, User user) {
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

    public Post returnPostByID(int postID) {
        Post post = postRepository.findById(postID);
        if (post != null) {
            return post;
        }
        return null;
    }

    public Page<Post> getUserPosts(int userID, int pageNumber, int limit) {
        return postRepository.findByUserIdOrderByTimestampDesc(new PageRequest(pageNumber, limit), userID);
    }

    public Page<Post> getUsersPublicPosts(int userID, int pageNumber, int limit) {
        return postRepository.findByUserIdAndIsPublicOrderByTimestampDesc(new PageRequest(pageNumber, limit), userID, (byte) 1);
    }

    public Page<Post> getNetworkPosts(List userIDs, int pageNumber, int limit) {
        return postRepository.findByUserIdInOrderByTimestampDesc(new PageRequest(pageNumber, limit), userIDs);
    }

    public Page getNetworkPostsAndFriendInterestPosts(List friendsIDs, int userID, int pageNumber, int limit) {
        return postRepository.findAllPostsFromFriendsInterestsWithPagination(new PageRequest(pageNumber, limit), friendsIDs, userID);
    }

    public List getInterestingPostsAndFriendsIDs(List friendsIDs, int userID) {
        return postRepository.friendsIDsAndInterestingPostsIDS(friendsIDs, userID);
    }

    public List getCommentedPostsAndFriendsIDs(List friendsIDs, int userID) {
        return postRepository.friendsIDsAndCommentedPostsIDS(friendsIDs, userID);
    }
}
