package com.code.monks.nukkad.exception;

//import com.code.monks.nukkad.exceptionHanlder.OrderNotFoundException;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(DefaultAddressUpdateNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleDefaultAddressUpdateNotAllowed(DefaultAddressUpdateNotAllowedException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(),ex.getError().getResponseCode());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(),LocalDateTime.now(),ex.getError().getResponseCode());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		log.error("Validation error: {}", ex.getMessage(), ex);

		List<String> messages = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.toList());

		String errorMessage = String.join(", ", messages);

		ErrorResponse error = new ErrorResponse("Validation failed: " + errorMessage, LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<ErrorResponse> handleExternalServerErrors(ExternalServiceException ex) {
		ErrorResponse error = new ErrorResponse("Internal Server Error: " + ex.getMessage(), LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceErrors(DuplicateResourceException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(), ex.getError().getResponseCode());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UnhandledException.class)
	public ResponseEntity<ErrorResponse> handleUnhandledException(UnhandledException ex) {
		ErrorResponse error = new ErrorResponse(ex.getErrorCode().getMessage(), ex.getErrorCode().getResponseCode());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse error = new ErrorResponse("Internal Server Error: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

    @ExceptionHandler(com.code.monks.nukkad.exception.OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(com.code.monks.nukkad.exception.OrderNotFoundException exception)
    {
        ErrorResponse response= new ErrorResponse
				("Order Not Found :" + LocalDateTime.now(), 400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(),LocalDateTime.now(),HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

}
