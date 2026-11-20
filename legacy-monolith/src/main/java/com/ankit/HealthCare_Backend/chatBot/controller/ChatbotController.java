package com.ankit.HealthCare_Backend.chatBot.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.chatBot.dto.chatResponse;
import com.ankit.HealthCare_Backend.chatBot.service.ChatbotService;

@Tag(name = "Chatbot", description = "AI Health Assistant — requires PATIENT role JWT token")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/patient/chatbot")
@Validated
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "Ask the AI Health Assistant a question",
            description = "Sends the patient's message to the LLM and returns a general-info reply. " +
                    "Never used for diagnosis or prescriptions.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI reply returned"),
            @ApiResponse(responseCode = "400", description = "Validation error — empty or too-long message"),
            @ApiResponse(responseCode = "503", description = "AI provider unavailable — try again later")
    })
    @PostMapping("/ask")
    public ResponseEntity<chatResponse> ask(@Valid @RequestBody com.ankit.HealthCare_Backend.chatBot.dto.ChatRequestDTO chatRequestDTO) {
        chatResponse response;
        response = chatbotService.getChatResponse(chatRequestDTO.getMessage());
        return ResponseEntity.ok(response);
    }
}