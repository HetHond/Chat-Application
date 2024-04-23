package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.entities.dto.UserBasicDto;
import com.hethond.chatbackend.services.AuthenticationService;
import com.hethond.chatbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(UserService userService,
                                    AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public record LoginBody(String username, String password) {}

    // TODO -- Implement token refreshing
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginBody body) {
        String jwtToken = authenticationService.authenticate(body.username, body.password);
        return ResponseEntity.ok(ApiResponse.success(jwtToken));
    }

    // TODO -- Add the ability to activate a account with email of phone number

    public record CreationBody(String username, String password) {}

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserBasicDto>> register(@RequestBody CreationBody body) {
        User user = userService.createUser(
                body.username(),
                body.password()
        );
        UserBasicDto userDto = UserBasicDto.fromUser(user);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }
}
