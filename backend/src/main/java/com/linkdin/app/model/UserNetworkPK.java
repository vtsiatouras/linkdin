package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserNetworkPK implements Serializable {
    private int usersUserId;
    private int usersUserId1;

    @Column(name = "Users_user_id")
    @Id
    public int getUsersUserId() {
        return usersUserId;
    }

    public void setUsersUserId(int usersUserId) {
        this.usersUserId = usersUserId;
    }

    @Column(name = "Users_user_id1")
    @Id
    public int getUsersUserId1() {
        return usersUserId1;
    }

    public void setUsersUserId1(int usersUserId1) {
        this.usersUserId1 = usersUserId1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNetworkPK that = (UserNetworkPK) o;
        return usersUserId == that.usersUserId &&
                usersUserId1 == that.usersUserId1;
    }

    @Override
    public int hashCode() {

        return Objects.hash(usersUserId, usersUserId1);
    }
}
