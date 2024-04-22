package com.example.SMS.repository;

import com.example.SMS.entity.ChatRoom;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(User senderId, User recipientId);
}
