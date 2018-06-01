package com.linkdin.app.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class User {
    private int userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userSurname;
    private String userPhoneNumber;
    private byte[] userProfilePicture;
    private String userCity;
    private String userProfession;
    private String userCompany;
    private Byte isAdmin;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_email")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Basic
    @Column(name = "user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_surname")
    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    @Basic
    @Column(name = "user_phone_number")
    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    @Basic
    @Column(name = "user_profile_picture")
    public byte[] getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(byte[] userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    @Basic
    @Column(name = "user_city")
    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    @Basic
    @Column(name = "user_profession")
    public String getUserProfession() {
        return userProfession;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }

    @Basic
    @Column(name = "user_company")
    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    @Basic
    @Column(name = "is_admin")
    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(userEmail, user.userEmail) &&
                Objects.equals(userPassword, user.userPassword) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userSurname, user.userSurname) &&
                Objects.equals(userPhoneNumber, user.userPhoneNumber) &&
                Arrays.equals(userProfilePicture, user.userProfilePicture) &&
                Objects.equals(userCity, user.userCity) &&
                Objects.equals(userProfession, user.userProfession) &&
                Objects.equals(userCompany, user.userCompany) &&
                Objects.equals(isAdmin, user.isAdmin);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(userId, userEmail, userPassword, userName, userSurname, userPhoneNumber, userCity, userProfession, userCompany, isAdmin);
        result = 31 * result + Arrays.hashCode(userProfilePicture);
        return result;
    }
}
