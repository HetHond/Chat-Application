package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.Channel;
import com.hethond.chatbackend.entities.Message;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.entities.dto.MessageBasicDto;
import com.hethond.chatbackend.services.ChannelService;
import com.hethond.chatbackend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;
    private final ChannelService channelService;

    @Autowired
    public MessageController(final MessageService messageService,
                             final ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    // TODO -- Add pages
    @GetMapping("/channels/{channelId}/messages")
    public ResponseEntity<ApiResponse<List<MessageBasicDto>>> getChannelMessages(
            @PathVariable long channelId) {
        List<Message> messages = messageService.findAllMessagesByChannelId(channelId);
        List<MessageBasicDto> messageDtoList = messages.stream().map(MessageBasicDto::fromMessage).toList();
        return ResponseEntity.ok(ApiResponse.success(messageDtoList));
    }

    public record MessageCreationObject(String content) {}
    @PostMapping("/channels/{channelId}/messages")
    public ResponseEntity<ApiResponse<MessageBasicDto>> addChannelMessage(
            @RequestBody MessageCreationObject messageCreationObject,
            @PathVariable long channelId) {
        Channel channel = channelService.findChannelById(channelId);
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message createdMessage = messageService.saveMessage(
                new Message(author, channel, messageCreationObject.content())
        );
        MessageBasicDto createdMessageDto = MessageBasicDto.fromMessage(createdMessage);
        return ResponseEntity.ok(ApiResponse.success(createdMessageDto));
    }
}
