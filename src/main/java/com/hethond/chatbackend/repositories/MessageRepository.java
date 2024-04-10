package com.hethond.chatbackend.repositories;

import com.hethond.chatbackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChannelId(long channelId);
}
