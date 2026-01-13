package com.project.global.exception.code.domain;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getCustomCode();
    String getMessage();
}
