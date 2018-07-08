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
        User user = repository.findByuserEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean authenticate(String email, String password) {
        User user = repository.findByuserEmail(email);
        if (user != null && user.getUserPassword() == password) { // TODO hash klp
            return true;
        }
        return false;
    }
}
