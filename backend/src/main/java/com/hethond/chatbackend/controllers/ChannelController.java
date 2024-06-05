package com.hethond.chatbackend.controllers;

import com.hethond.chatbackend.response.ApiResponse;
import com.hethond.chatbackend.entities.Channel;
import com.hethond.chatbackend.entities.dto.ChannelWithUsersDto;
import com.hethond.chatbackend.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChannelWithUsersDto>> getChannel(@PathVariable long id) {
        Channel channel = channelService.findChannelById(id);
        return ResponseEntity.ok(ApiResponse.success(ChannelWithUsersDto.fromChannel(channel)));
    }

    // TODO -- Implement a method for adding members to a channel
}
