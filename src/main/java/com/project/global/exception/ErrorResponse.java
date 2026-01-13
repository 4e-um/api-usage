package com.project.global.exception;

public record ErrorResponse(
        int statusCode,
        String code,
        String message) {
}
