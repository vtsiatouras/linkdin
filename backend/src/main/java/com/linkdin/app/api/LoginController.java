//package com.linkdin.app.api;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.linkdin.app.dto.UserDTO;
//import com.linkdin.app.model.User;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class LoginController {
//
//    public class LoginCredentials {
//        public String email;
//        public String password;
//    }
//    @PostMapping(path = "/login")
//    public ResponseEntity<String> login(@RequestBody LoginCredentials loginCredentials) {
////        System.out.println("aaaa");
//        System.out.println(loginCredentials.email + " " + loginCredentials.password);
//        return ResponseEntity.ok(loginCredentials.email);
//    }
//}
