package com.ankit.HealthCare_Backend.chatBot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ChatRequestDTO {
    @NotEmpty(message="message can not be empty ")
    @Size(max=1000,message="message can not be greater than 1000 chars")
    private String message;
}
