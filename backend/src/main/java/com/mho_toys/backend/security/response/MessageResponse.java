package com.mho_toys.backend.security.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    
    private String message;
    private LocalDateTime timestamp;
    private boolean success = true;

    public MessageResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.success = true;
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.success = success;
    }

    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
} 