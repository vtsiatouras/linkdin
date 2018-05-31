package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostCommentEntityPK implements Serializable {
    private int commentId;
    private int postPostId;
    private int postUserUserId;

    @Column(name = "comment_id")
    @Id
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @Column(name = "Post_post_id")
    @Id
    public int getPostPostId() {
        return postPostId;
    }

    public void setPostPostId(int postPostId) {
        this.postPostId = postPostId;
    }

    @Column(name = "Post_User_user_id")
    @Id
    public int getPostUserUserId() {
        return postUserUserId;
    }

    public void setPostUserUserId(int postUserUserId) {
        this.postUserUserId = postUserUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCommentEntityPK that = (PostCommentEntityPK) o;
        return commentId == that.commentId &&
                postPostId == that.postPostId &&
                postUserUserId == that.postUserUserId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(commentId, postPostId, postUserUserId);
    }
}
