package com.linkdin.app.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@IdClass(PostPK.class)
public class Post {
    private int id;
    private Serializable content;
    private Timestamp timestamp;
    private Byte isAdvertisment;
    private int userId;
    private int userUserNetworkUserId;

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
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "user_user_network_user_id")
    public int getUserUserNetworkUserId() {
        return userUserNetworkUserId;
    }

    public void setUserUserNetworkUserId(int userUserNetworkUserId) {
        this.userUserNetworkUserId = userUserNetworkUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                userId == post.userId &&
                userUserNetworkUserId == post.userUserNetworkUserId &&
                Objects.equals(content, post.content) &&
                Objects.equals(timestamp, post.timestamp) &&
                Objects.equals(isAdvertisment, post.isAdvertisment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content, timestamp, isAdvertisment, userId, userUserNetworkUserId);
    }
}
