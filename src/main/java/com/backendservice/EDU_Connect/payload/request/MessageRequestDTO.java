package com.backendservice.EDU_Connect.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequestDTO {
    @NotNull(message = "Sender ID cannot be null")
    private Long senderId;

    @NotNull(message = "Receiver ID cannot be null")
    private Long receiverId;

    @NotBlank(message = "Message content cannot be blank")
    private String content;
}
