package com.linkdin.app.api;

import com.linkdin.app.dto.Credentials;
import com.linkdin.app.model.User;
import com.linkdin.app.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpServletRequest request) {
        System.out.println(credentials.email + " " + credentials.password);

        // Check if credentials are legit
        // Check for empty fields
        if(credentials.checkForEmptyCreds(credentials)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // Authenticate user
        if (userService.authenticate(credentials.email, credentials.password)) {
            User user = userService.returnUser(credentials.email);
            String id = Integer.toString(user.getId());
            String email = user.getEmail();
            String firstName = user.getName();
            String lastName = user.getSurname();
             // Random User token
            Random rand = new Random();
            int userTokenInt = rand.nextInt(9000000) + 1000000;
            String userToken = Integer.toString(userTokenInt);

            String jsonString = new JSONObject()
                    .put("userToken", userToken)
                    .put("userID", id)
                    .put("firstName", firstName)
                    .put("lastName", lastName)
                    .put("email", email)
                    .toString();

//            System.err.println(jsonString);
            // Create a new session and add the security context.
            HttpSession session = request.getSession(true);
            session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, id);
            session.setAttribute("userToken", userToken);
            return ResponseEntity.ok(jsonString);
        } else {
            System.err.println("bad creds");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("BAD_CREDENTIALS");
        }
    }

}

