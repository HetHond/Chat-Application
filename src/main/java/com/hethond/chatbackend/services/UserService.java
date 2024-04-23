package com.hethond.chatbackend.services;

import com.hethond.chatbackend.ApiException;
import com.hethond.chatbackend.entities.Channel;
import com.hethond.chatbackend.entities.Role;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.repositories.ChannelRepository;
import com.hethond.chatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ChannelService channelService;

    @Autowired
    public UserService(UserRepository userRepository,
                       ChannelService channelService) {
        this.userRepository = userRepository;
        this.channelService = channelService;
    }

    public List<User> findAllUsers() { return userRepository.findAll(); }

    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> ApiException.notFound("User not found"));
    }

    public User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> ApiException.notFound("User not found"));
    }

    public User createUser(String username, String password) {
        User newUser = new User(username, BCrypt.hashpw(password, BCrypt.gensalt()), Role.USER);
        Channel welcomeChannel = channelService.findChannelById(2);
        welcomeChannel.addMember(newUser);
        return userRepository.save(newUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
