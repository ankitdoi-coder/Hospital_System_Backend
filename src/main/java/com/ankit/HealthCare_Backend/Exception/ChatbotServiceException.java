package com.ankit.HealthCare_Backend.Exception;

/**
 * Thrown when the Groq (LLM) API call fails — timeout, rate limit, invalid key, etc.
 * Mapped to HTTP 503 in GlobalExceptionHandler so the frontend gets a clean,
 * user-friendly error instead of a raw stack trace.
 */
public class ChatbotServiceException extends RuntimeException {
    public ChatbotServiceException(String message) {
        super(message);
    }

    public ChatbotServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}