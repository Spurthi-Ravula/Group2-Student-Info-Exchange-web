package com.example.SMS.liveChat.chat;

import com.example.SMS.entity.ChatMessage;
import com.example.SMS.entity.User;
import com.example.SMS.liveChat.chatroom.ChatRoomService;
import com.example.SMS.repository.ChatMessageRepository;
import com.example.SMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    public ChatMessage save(ChatMessageDto chatMessageDto) {
        User sender = userRepository.findByEmail(chatMessageDto.getSenderId());
        User recipient = userRepository.findByEmail(chatMessageDto.getRecipientId());

        var chatId = chatRoomService
                .getChatRoomId(sender, recipient, true)
                .orElseThrow(); // You can create your own dedicated exception

        ChatMessage chatMessage = ChatMessage.builder()
                .chatId(chatId)
                .senderId(sender)
                .recipientId(recipient)
                .content(chatMessageDto.getContent())
                .timestamp(new Timestamp(Instant.now().toEpochMilli()))
                .build();
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(userRepository.findByEmail(senderId),
                userRepository.findByEmail(recipientId), true);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
