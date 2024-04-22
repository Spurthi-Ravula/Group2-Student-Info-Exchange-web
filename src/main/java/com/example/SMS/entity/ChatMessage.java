package com.example.SMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipientId;

    private String content;
    private Timestamp timestamp;
}
