package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find all messages where the user is either the sender or receiver
    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId ORDER BY m.sentAt ASC")
    List<Message> findBySenderIdOrReceiverId(Long userId);

    // Find conversation between two users (sender and receiver)
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.sentAt ASC")
    List<Message> findConversation(Long senderId, Long receiverId);

    // Optional: Find all messages sent by a user
    List<Message> findBySenderId(Long senderId);

    // Optional: Find all messages received by a user
    List<Message> findByReceiverId(Long receiverId);
}