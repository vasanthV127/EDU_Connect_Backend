package com.backendservice.EDU_Connect.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageResponseDTO {
    private Long id;
    private Long senderId;
    private String senderName; // Optional: Include sender's name for UI convenience
    private Long receiverId;
    private String receiverName; // Optional: Include receiver's name
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;
}
