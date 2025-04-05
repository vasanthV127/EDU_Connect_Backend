package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.Message;
import com.backendservice.EDU_Connect.payload.request.MessageRequestDTO;
import com.backendservice.EDU_Connect.payload.response.ChatMessageResponseDTO;
import com.backendservice.EDU_Connect.payload.response.MessageResponseDTO;
import com.backendservice.EDU_Connect.security.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageRequestDTO messageRequest) {
        Message message = messageService.sendMessage(
                messageRequest.getSenderId(),
                messageRequest.getReceiverId(),
                messageRequest.getContent()
        );
        MessageResponseDTO response = new MessageResponseDTO();
        response.setId(message.getId());
        response.setSenderId(message.getSender().getId());
        response.setReceiverId(message.getReceiver().getId());
        response.setContent(message.getContent());
        response.setSentAt(message.getSentAt());
        response.setRead(message.isRead());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatMessageResponseDTO>> getMessagesForUser(@PathVariable Long userId) {
        List<ChatMessageResponseDTO> messages = messageService.getMessagesForUser(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<ChatMessageResponseDTO>> getConversation(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        List<ChatMessageResponseDTO> conversation = messageService.getConversation(senderId, receiverId);
        return ResponseEntity.ok(conversation);
    }
}