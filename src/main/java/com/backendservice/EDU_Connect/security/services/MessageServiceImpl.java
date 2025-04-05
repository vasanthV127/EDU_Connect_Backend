package com.backendservice.EDU_Connect.security.services;

import com.backendservice.EDU_Connect.model.Message;
import com.backendservice.EDU_Connect.model.User;
import com.backendservice.EDU_Connect.payload.response.ChatMessageResponseDTO;
import com.backendservice.EDU_Connect.repository.MessageRepository;
import com.backendservice.EDU_Connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository; // Assume this exists
    @Autowired
    private UserRepository userRepository; // Assume this exists

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        return messageRepository.save(message);
    }

    @Override
    public List<ChatMessageResponseDTO> getMessagesForUser(Long userId) {
        List<Message> messages = messageRepository.findBySenderIdOrReceiverId(userId);
        return messages.stream().map(msg -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSender().getId());
            dto.setSenderName(msg.getSender().getName());
            dto.setReceiverId(msg.getReceiver().getId());
            dto.setReceiverName(msg.getReceiver().getName());
            dto.setContent(msg.getContent());
            dto.setSentAt(msg.getSentAt());
            dto.setRead(msg.isRead());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageResponseDTO> getConversation(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findConversation(senderId, receiverId);
        return messages.stream().map(msg -> {
            ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSender().getId());
            dto.setSenderName(msg.getSender().getName());
            dto.setReceiverId(msg.getReceiver().getId());
            dto.setReceiverName(msg.getReceiver().getName());
            dto.setContent(msg.getContent());
            dto.setSentAt(msg.getSentAt());
            dto.setRead(msg.isRead());
            return dto;
        }).collect(Collectors.toList());
    }
}