package com.linkdin.app.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "UserNetwork", schema = "web_dev_db", catalog = "")
@IdClass(UserNetworkEntityPK.class)
public class UserNetworkEntity {
    private int usersUserId;
    private int usersUserId1;

    @Id
    @Column(name = "Users_user_id")
    public int getUsersUserId() {
        return usersUserId;
    }

    public void setUsersUserId(int usersUserId) {
        this.usersUserId = usersUserId;
    }

    @Id
    @Column(name = "Users_user_id1")
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
        UserNetworkEntity that = (UserNetworkEntity) o;
        return usersUserId == that.usersUserId &&
                usersUserId1 == that.usersUserId1;
    }

    @Override
    public int hashCode() {

        return Objects.hash(usersUserId, usersUserId1);
    }
}
