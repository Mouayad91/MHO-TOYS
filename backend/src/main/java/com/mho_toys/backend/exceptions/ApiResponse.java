package com.mho_toys.backend.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ApiResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;

    public ApiResponse() {}

    public ApiResponse(LocalDateTime timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

}

