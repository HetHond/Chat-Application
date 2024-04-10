package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.Message;
import com.hethond.chatbackend.entities.dto.MessageBasicDto;
import com.hethond.chatbackend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    // TODO -- Add pages
    @GetMapping("/channels/{channelId}/messages")
    public ResponseEntity<ApiResponse<List<MessageBasicDto>>> getChannelMessages(
            @PathVariable("channelId") long channelId) {
        List<Message> messages = messageService.findAllMessagesByChannelId(channelId);
        List<MessageBasicDto> messageDtoList = messages.stream().map(MessageBasicDto::fromMessage).toList();
        return ResponseEntity.ok(ApiResponse.success(messageDtoList));
    }

    // TODO -- Create messages
}
