package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public record LoginBody(String username, String password) {}

    // TODO -- Implement token refreshing
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginBody body) {
        String jwtToken = authenticationService.authenticate(body.username, body.password);
        return ResponseEntity.ok(ApiResponse.success(jwtToken));
    }
}
