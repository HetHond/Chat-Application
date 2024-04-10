package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.Channel;

import java.util.Set;
import java.util.stream.Collectors;

public class ChannelWithUsersDto extends ChannelBasicDto {
    public static ChannelWithUsersDto fromChannel(Channel channel) {
        return new ChannelWithUsersDto(channel.getId(), channel.getName(),
                channel.getMembers().stream().map(UserBasicDto::fromUser).collect(Collectors.toSet()));
    }

    private final Set<UserBasicDto> members;

    protected ChannelWithUsersDto(long id, String name, Set<UserBasicDto> members) {
        super(id, name);
        this.members = members;
    }

    public Set<UserBasicDto> getMembers() {
        return members;
    }
}
