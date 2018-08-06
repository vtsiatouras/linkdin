package com.linkdin.app.services;

import com.linkdin.app.dto.UserAttributes;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AuthRequestService {
    public boolean authenticateRequest(UserAttributes userAttributes, HttpSession session) {
        if (session == null) {
            return false;
        }

        if (userAttributes.userToken == null || userAttributes.email == null) {
            return false;
        }

        if (session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).equals(userAttributes.email) &&
                session.getAttribute("userToken").equals(userAttributes.userToken)) {
            System.err.println("user is authenticated!");
            return true;
        } else {
            System.err.println("user is NOT authenticated!");
            return false;
        }
    }
}
