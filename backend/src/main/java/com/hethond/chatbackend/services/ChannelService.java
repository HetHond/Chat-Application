package com.hethond.chatbackend.services;

import com.hethond.chatbackend.exceptions.ApiException;
import com.hethond.chatbackend.entities.Channel;
import com.hethond.chatbackend.repositories.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel findChannelById(long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel.orElseThrow(() -> ApiException.notFound("Channel not found"));
    }

    @Transactional
    public Channel saveChannel(Channel channel) {
        return channelRepository.save(channel);
    }
}
