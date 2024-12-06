package com.crm.crm.exception;

import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.util.APIResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponseDTO<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(APIResponseUtil.createFailureResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<APIResponseDTO<String>> handleInvalidInputException(InvalidInputException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponseUtil.createFailureResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseDTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponseUtil.createFailureResponse("An unexpected error occurred"));
    }
}
