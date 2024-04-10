package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.entities.dto.UserWithChannelsDto;
import com.hethond.chatbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO -- This may only be used by Admins
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserWithChannelsDto>>> getAllUsers() {
        List<UserWithChannelsDto> users = userService.findAllUsers()
                .stream().map(UserWithChannelsDto::fromUser).toList();
        ApiResponse<List<UserWithChannelsDto>> response = ApiResponse.success(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserWithChannelsDto>> getUserById(@PathVariable long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(ApiResponse.success(UserWithChannelsDto.fromUser(user)));
    }

    // TODO -- Implement update user method
}
