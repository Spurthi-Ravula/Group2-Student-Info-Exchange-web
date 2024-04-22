package com.example.SMS.liveChat.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;


}
