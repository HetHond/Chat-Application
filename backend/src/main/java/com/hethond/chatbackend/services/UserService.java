package com.hethond.chatbackend.services;

import com.hethond.chatbackend.entities.AccountStatus;
import com.hethond.chatbackend.entities.Role;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.exceptions.NotFoundException;
import com.hethond.chatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    // TODO add logging to this service

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() { return userRepository.findAll(); }

    public User findUserById(final UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("Could not find user by id [" + id + "]."));
    }

    public User findUserByUsername(final String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new NotFoundException("Could not find user by id [" + username + "]."));
    }

    public User findUserByPhone(final String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        return user.orElseThrow(() -> new NotFoundException("Could not find user by phone number [" + phone + "]."));
    }

    public User createUser(final String username,
                           final String password,
                           final Role role,
                           final AccountStatus accountStatus) {
        return createUser(null, username, password, role, accountStatus);
    }

    public User createUser(final String phone,
                           final String username,
                           final String password,
                           final Role role,
                           final AccountStatus accountStatus) {
        final User newUser = new User(phone, username, BCrypt.hashpw(password, BCrypt.gensalt()), role, accountStatus);
        return userRepository.save(newUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
