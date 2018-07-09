package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserPK implements Serializable {
    private int id;
    private int userNetworkId;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_network_id")
    @Id
    public int getUserNetworkId() {
        return userNetworkId;
    }

    public void setUserNetworkId(int userNetworkId) {
        this.userNetworkId = userNetworkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPK userPK = (UserPK) o;
        return id == userPK.id &&
                userNetworkId == userPK.userNetworkId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userNetworkId);
    }
}
