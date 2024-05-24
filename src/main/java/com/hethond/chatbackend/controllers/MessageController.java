package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.ApiException;
import com.hethond.chatbackend.ApiResponse;
import com.hethond.chatbackend.entities.Channel;
import com.hethond.chatbackend.entities.Message;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.entities.dto.MessageBasicDto;
import com.hethond.chatbackend.services.ChannelService;
import com.hethond.chatbackend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;
    private final ChannelService channelService;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(final MessageService messageService,
                             final ChannelService channelService,
                             final SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.messagingTemplate = messagingTemplate;
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
        if (!channel.hasMemberWithId(author.getId()))
            throw new ApiException(HttpStatus.FORBIDDEN.value(),
                    "You don't have permission to create messages in this channel.");

        Message createdMessage = messageService.saveMessage(
                new Message(author, channel, messageCreationObject.content())
        );
        MessageBasicDto createdMessageDto = MessageBasicDto.fromMessage(createdMessage);

        // TODO -- separate concerns (create a service for this part)
        for (User recipient : channel.getMembers()) {
            messagingTemplate.convertAndSendToUser(recipient.getId().toString(), "/topic/messages", createdMessageDto);
        }

        return ResponseEntity.ok(ApiResponse.success(createdMessageDto));
    }
}
