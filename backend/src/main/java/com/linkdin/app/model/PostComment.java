package com.linkdin.app.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "post_comment", schema = "web_dev_db", catalog = "")
@IdClass(PostCommentPK.class)
public class PostComment {
    private int id;
    private Serializable content;
    private Timestamp commentTimestamp;
    private int postId;
    private int postUserId;
    private int postUserUserNetworkUserId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content")
    public Serializable getContent() {
        return content;
    }

    public void setContent(Serializable content) {
        this.content = content;
    }

    @Basic
    @Column(name = "comment_timestamp")
    public Timestamp getCommentTimestamp() {
        return commentTimestamp;
    }

    public void setCommentTimestamp(Timestamp commentTimestamp) {
        this.commentTimestamp = commentTimestamp;
    }

    @Id
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Id
    @Column(name = "post_user_id")
    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    @Id
    @Column(name = "post_user_user_network_user_id")
    public int getPostUserUserNetworkUserId() {
        return postUserUserNetworkUserId;
    }

    public void setPostUserUserNetworkUserId(int postUserUserNetworkUserId) {
        this.postUserUserNetworkUserId = postUserUserNetworkUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return id == that.id &&
                postId == that.postId &&
                postUserId == that.postUserId &&
                postUserUserNetworkUserId == that.postUserUserNetworkUserId &&
                Objects.equals(content, that.content) &&
                Objects.equals(commentTimestamp, that.commentTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content, commentTimestamp, postId, postUserId, postUserUserNetworkUserId);
    }
}
