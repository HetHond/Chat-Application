package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.User;

import java.util.UUID;

public class UserBasicDto {
    public static UserBasicDto fromUser(User user) {
        return new UserBasicDto(user.getId(), user.getPhone(), user.getUsername());
    }

    private final UUID id;
    private final String phone;
    private final String username;

    protected UserBasicDto(UUID id, String phone, String username) {
        this.id = id;
        this.phone = phone;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getPhone() { return phone; }

    public String getUsername() {
        return username;
    }
}
