package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.User;

public class UserBasicDto {
    public static UserBasicDto fromUser(User user) {
        return new UserBasicDto(user.getId(), user.getUsername());
    }

    private final long id;
    private final String username;

    protected UserBasicDto(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
