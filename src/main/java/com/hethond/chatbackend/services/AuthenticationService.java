package com.hethond.chatbackend.services;

import com.hethond.chatbackend.ApiException;
import com.hethond.chatbackend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(UserService userService,
                                 JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {
        // TODO -- This might not be the cleanest way of going about it
        final User selectedUser;
        try {
            selectedUser = userService.findUserByUsername(username);
        } catch (ApiException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password");
        }

        if (!BCrypt.checkpw(password, selectedUser.getPasswordHash()))
            throw new ApiException(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password");

        return jwtService.generateToken(selectedUser);
    }
}
