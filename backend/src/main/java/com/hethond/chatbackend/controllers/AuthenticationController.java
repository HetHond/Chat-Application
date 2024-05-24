package com.hethond.chatbackend.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hethond.chatbackend.ApiException;
import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.AccountStatus;
import com.hethond.chatbackend.entities.Role;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.entities.dto.UserBasicDto;
import com.hethond.chatbackend.services.AuthenticationService;
import com.hethond.chatbackend.services.UserService;
import com.hethond.chatbackend.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final VerificationService verificationService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(UserService userService,
                                    VerificationService verificationService,
                                    AuthenticationService authenticationService) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.authenticationService = authenticationService;
    }

    public record LoginBody(String username, String password) {}

    // TODO -- Implement token refreshing
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginBody body) {
        String sessionToken = authenticationService.authenticate(body.username, body.password);
        return ResponseEntity.ok(ApiResponse.success(sessionToken));
    }

    public record VerificationBody(@JsonProperty("phone") String phone,
                                   @JsonProperty("code") String verificationCode) {}

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verify(@RequestBody VerificationBody body) {
        final User user = userService.findUserByPhone(body.phone());
        verificationService.verifyCode(user.getPhone(), body.verificationCode());
        if (user.getAccountStatus() == AccountStatus.INACTIVE) {
            user.setAccountStatus(AccountStatus.ACTIVE);
            userService.saveUser(user);
        }
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    public record SignupBody(String phone, String username, String password) {}

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserBasicDto>> register(@RequestBody SignupBody body) {
        try {
            User newUser = userService.createUser(
                    body.phone(),
                    body.username(),
                    body.password(),
                    Role.USER,
                    AccountStatus.INACTIVE
            );

            verificationService.generateAndSendCode(newUser.getPhone());

            UserBasicDto userDto = UserBasicDto.fromUser(newUser);
            return ResponseEntity.ok(ApiResponse.success(userDto));
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "An account already exists with this username or phone number.");
        }
    }
}
