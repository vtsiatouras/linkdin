package com.linkdin.app.services;

import com.linkdin.app.dto.SearchResults;
import com.linkdin.app.dto.UserBasicInfo;
import com.linkdin.app.model.User;
import com.linkdin.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    public boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User returnUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }

    public User returnUserByID(Integer id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean authenticate(String email, String password) {
        System.err.println(email);
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // TODO hash klp
            return true;
        }
        return false;
    }

    public void storeUser(User user) {
        userRepository.save(user);
    }

    public SearchResults searchUsers(String queryName) {
        List<User> list = userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(queryName, queryName);
        ArrayList<UserBasicInfo> resultList = new ArrayList<UserBasicInfo>();
        for (User element : list) {
            UserBasicInfo userSearchResult = new UserBasicInfo();
            userSearchResult.id = Integer.toString(element.getId());
            userSearchResult.name = element.getName();
            userSearchResult.surname = element.getSurname();
            userSearchResult.image = imageStorageService.getImage(element.getProfilePicture());
            resultList.add(userSearchResult);
        }
        String totalResults = Integer.toString(resultList.size());
        SearchResults searchResults = new SearchResults();
        searchResults.list = resultList;
        searchResults.numberOfResults = totalResults;
        return searchResults;
    }
}
