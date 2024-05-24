package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserWithChannelsDto extends UserBasicDto {
    public static UserWithChannelsDto fromUser(User user) {
        return new UserWithChannelsDto(user.getId(), user.getPhone(), user.getUsername(),
                user.getChannels().stream().map(ChannelBasicDto::fromChannel).collect(Collectors.toSet()));
    }

    private final Set<ChannelBasicDto> channels;

    protected UserWithChannelsDto(UUID id, String phone, String username, Set<ChannelBasicDto> channels) {
        super(id, phone, username);
        this.channels = channels;
    }

    public Set<ChannelBasicDto> getChannels() {
        return channels;
    }
}
