package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostCommentPK implements Serializable {
    private int id;
    private int postId;
    private int postUserId;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "post_id")
    @Id
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Column(name = "post_user_id")
    @Id
    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCommentPK that = (PostCommentPK) o;
        return id == that.id &&
                postId == that.postId &&
                postUserId == that.postUserId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, postId, postUserId);
    }
}
