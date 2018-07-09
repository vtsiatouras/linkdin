package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostPK implements Serializable {
    private int id;
    private int userId;
    private int userUserNetworkUserId;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "user_user_network_user_id")
    @Id
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
        PostPK postPK = (PostPK) o;
        return id == postPK.id &&
                userId == postPK.userId &&
                userUserNetworkUserId == postPK.userUserNetworkUserId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, userUserNetworkUserId);
    }
}
