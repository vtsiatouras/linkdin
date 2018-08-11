package com.linkdin.app.services;

import com.linkdin.app.dto.UserSearchResult;
import com.linkdin.app.model.User;
import com.linkdin.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public boolean emailExist(String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User returnUser(String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }

    public User returnUserByID(Integer id) {
        User user = repository.findById(id);
        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean authenticate(String email, String password) {
        System.err.println(email);
        User user = repository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // TODO hash klp
            return true;
        }
        return false;
    }

    public void storeUser(User user) {
        repository.save(user);
    }

    public List searchUsers(String queryName) {
        List<User> list = repository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(queryName, queryName);
        ArrayList<UserSearchResult> returnList = new ArrayList<UserSearchResult>();
        for (User element : list) {
            UserSearchResult userSearchResult = new UserSearchResult();
            userSearchResult.id = Integer.toString(element.getId());
            userSearchResult.name = element.getName();
            userSearchResult.surname = element.getSurname();
            returnList.add(userSearchResult);
        }
        return returnList;
    }
}
