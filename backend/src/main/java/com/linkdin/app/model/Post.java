package com.linkdin.app.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@IdClass(PostPK.class)
public class Post {
    private int postId;
    private Serializable postContent;
    private Timestamp postTimestamp;
    private Byte isAdvertisment;
    private int userUserId;

    @Id
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "post_content")
    public Serializable getPostContent() {
        return postContent;
    }

    public void setPostContent(Serializable postContent) {
        this.postContent = postContent;
    }

    @Basic
    @Column(name = "post_timestamp")
    public Timestamp getPostTimestamp() {
        return postTimestamp;
    }

    public void setPostTimestamp(Timestamp postTimestamp) {
        this.postTimestamp = postTimestamp;
    }

    @Basic
    @Column(name = "is_advertisment")
    public Byte getIsAdvertisment() {
        return isAdvertisment;
    }

    public void setIsAdvertisment(Byte isAdvertisment) {
        this.isAdvertisment = isAdvertisment;
    }

    @Id
    @Column(name = "User_user_id")
    public int getUserUserId() {
        return userUserId;
    }

    public void setUserUserId(int userUserId) {
        this.userUserId = userUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId &&
                userUserId == post.userUserId &&
                Objects.equals(postContent, post.postContent) &&
                Objects.equals(postTimestamp, post.postTimestamp) &&
                Objects.equals(isAdvertisment, post.isAdvertisment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(postId, postContent, postTimestamp, isAdvertisment, userUserId);
    }
}
