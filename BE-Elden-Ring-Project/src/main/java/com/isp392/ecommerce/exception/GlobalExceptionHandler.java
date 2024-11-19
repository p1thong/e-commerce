package com.isp392.ecommerce.exception;


import com.isp392.ecommerce.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//      ResponseEntity<ApiResponse<?>> handlingException(){
//
//        return ResponseEntity.internalServerError()
//                .body(ApiResponse.builder()
//                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                        .build());
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handlingAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())//Use status code in errorCode
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())//Set code
                        .message(errorCode.getMessage())//Set message
                        .build()
                );

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException{}", e.getMessage());
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }


    @ExceptionHandler(value = SQLException.class)
    ResponseEntity<ApiResponse<?>> handlingSQLServerException(SQLException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .code(exception.getSQLState())
                        .message(exception.getMessage())
                        .build());
    }
}
