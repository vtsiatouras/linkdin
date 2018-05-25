package com.linkdin.app.dbentities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "PostComment", schema = "web_dev_db", catalog = "")
@IdClass(PostCommentEntityPK.class)
public class PostCommentEntity {
    private int commentId;
    private Serializable commentContent;
    private Timestamp commentTimestamp;
    private int postPostId;
    private int postUserUserId;

    @Id
    @Column(name = "comment_id")
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "comment_content")
    public Serializable getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(Serializable commentContent) {
        this.commentContent = commentContent;
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
    @Column(name = "Post_post_id")
    public int getPostPostId() {
        return postPostId;
    }

    public void setPostPostId(int postPostId) {
        this.postPostId = postPostId;
    }

    @Id
    @Column(name = "Post_User_user_id")
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
        PostCommentEntity that = (PostCommentEntity) o;
        return commentId == that.commentId &&
                postPostId == that.postPostId &&
                postUserUserId == that.postUserUserId &&
                Objects.equals(commentContent, that.commentContent) &&
                Objects.equals(commentTimestamp, that.commentTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commentId, commentContent, commentTimestamp, postPostId, postUserUserId);
    }
}
