package com.hethond.chatbackend.entities.dto;

import com.hethond.chatbackend.entities.Message;

public class MessageBasicDto {
    public static MessageBasicDto fromMessage(Message message) {
        return new MessageBasicDto(message.getId(),
                UserBasicDto.fromUser(message.getAuthor()),
                ChannelBasicDto.fromChannel(message.getChannel()),
                message.getContent());
    }

    private final long id;
    private final UserBasicDto author;
    private final ChannelBasicDto channel;
    private final String content;

    protected MessageBasicDto(long id, UserBasicDto author, ChannelBasicDto channel, String content) {
        this.id = id;
        this.author = author;
        this.channel = channel;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public UserBasicDto getAuthor() {
        return author;
    }

    public ChannelBasicDto getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }
}
