package com.backendservice.EDU_Connect.security.services;

import com.backendservice.EDU_Connect.model.Message;
import com.backendservice.EDU_Connect.payload.response.ChatMessageResponseDTO;


import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId, Long receiverId, String content);
    List<ChatMessageResponseDTO> getMessagesForUser(Long userId);
    List<ChatMessageResponseDTO> getConversation(Long senderId, Long receiverId);
}