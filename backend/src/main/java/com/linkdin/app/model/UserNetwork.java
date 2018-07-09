package com.linkdin.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_network", schema = "web_dev_db", catalog = "")
public class UserNetwork {
    private int userId;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNetwork that = (UserNetwork) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId);
    }
}
