package com.linkdin.app.services;

import com.linkdin.app.model.Post;
import com.linkdin.app.model.PostInterest;
import com.linkdin.app.repositories.PostInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostInterestService {

    @Autowired
    PostInterestRepository postInterestRepository;

    public void addInterest(int postID, int userID) {
        // Check if user already is interested to this post
        if(postInterestRepository.findByPostIdAndUserId(postID, userID) != null){
            return;
        }
        // Save interest
        PostInterest postInterest = new PostInterest();
        postInterest.setPostId(postID);
        postInterest.setUserId(userID);
        postInterestRepository.save(postInterest);
    }

    public void deleteInterest(int postID, int userID) {
        PostInterest postInterest = postInterestRepository.findByPostIdAndUserId(postID, userID);
        if(postInterest != null){
            postInterestRepository.delete(postInterest);
        }
    }

    public List getInterestedUsers(int postID) {
        return postInterestRepository.findAllByPostId(postID);
    }
}
