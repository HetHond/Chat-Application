package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.Channel;

public class ChannelBasicDto {
    public static ChannelBasicDto fromChannel(Channel channel) {
        return new ChannelBasicDto(channel.getId(), channel.getName());
    }

    private final long id;
    private final String name;

    protected ChannelBasicDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
