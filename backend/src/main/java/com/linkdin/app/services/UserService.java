package com.linkdin.app.services;

import com.linkdin.app.model.User;
import com.linkdin.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public boolean emailExist(String email) {
        User user = repository.findByemail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User returnUser(String email) {
        User user = repository.findByemail(email);
        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean authenticate(String email, String password) {
        System.err.println(email);
        User user = repository.findByemail(email);
        if (user != null && user.getPassword().equals(password)) { // TODO hash klp
            return true;
        }
        return false;
    }

    public void storeUser(User user) {
        repository.save(user);
    }
}
