package com.linkdin.app.dto;

import com.linkdin.app.model.User;

public class UserRegister {

    public String email;
    public String password_1;
    public String password_2;
    public String firstName;
    public String lastName;
    public String phoneNumber;

    public boolean passwordCheck(UserRegister userRegister) {
        if(password_1.equals(password_2)){
            return true;
        }
        return false;
    }

    public User transformToUser(UserRegister userRegister) {
        User user = new User();
        user.setEmail(userRegister.email);
        user.setPassword(userRegister.password_1);
        user.setName(userRegister.firstName);
        user.setSurname(userRegister.lastName);
        user.setPhoneNumber(userRegister.phoneNumber);
        return user;
    }

}