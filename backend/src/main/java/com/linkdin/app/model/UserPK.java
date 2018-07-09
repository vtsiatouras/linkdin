package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserPK implements Serializable {
    private int id;
    private int userNetworkUserId;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_network_user_id")
    @Id
    public int getUserNetworkUserId() {
        return userNetworkUserId;
    }

    public void setUserNetworkUserId(int userNetworkUserId) {
        this.userNetworkUserId = userNetworkUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPK userPK = (UserPK) o;
        return id == userPK.id &&
                userNetworkUserId == userPK.userNetworkUserId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userNetworkUserId);
    }
}
