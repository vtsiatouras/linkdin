package com.linkdin.app.services;

import com.linkdin.app.model.User;
import com.linkdin.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDetailsImpl userDetailsImpl = null;

        User user = userRepository.findByemail(email);

        if(user == null){
            throw new UsernameNotFoundException("Login not found");
        } else{
            userDetailsImpl = new UserDetailsImpl(user);
        }

        return userDetailsImpl;
    }

}
