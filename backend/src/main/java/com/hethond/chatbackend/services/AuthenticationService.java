package com.hethond.chatbackend.services;

import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.exceptions.BadCredentialsException;
import com.hethond.chatbackend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public AuthenticationService(final UserService userService,
                                 final SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    public String authenticate(String username, String password) {
        final User selectedUser;
        try {
            selectedUser = userService.findUserByUsername(username);
        } catch (NotFoundException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        if (!BCrypt.checkpw(password, selectedUser.getPasswordHash()))
            throw new BadCredentialsException("Incorrect username or password");

        return sessionService.createSession(selectedUser.getId());
    }
}
