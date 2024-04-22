package com.example.SMS.liveChat.chat;

import com.example.SMS.entity.ChatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessage savedMsg = chatMessageService.save(chatMessageDto);
        ChatNotification chatNotification = new ChatNotification(
                savedMsg.getId(),
                savedMsg.getSenderId(),
                savedMsg.getRecipientId(),
                savedMsg.getContent()
        );
        		
        messagingTemplate.convertAndSendToUser(chatMessageDto.getRecipientId(), "/queue/messages", chatNotification);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
