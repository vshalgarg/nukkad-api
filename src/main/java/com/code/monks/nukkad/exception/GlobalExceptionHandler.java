package com.code.monks.nukkad.exception;



import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DefaultQrCodeNotUpdatedException.class)
	public ResponseEntity<ErrorResponse> handleDefaultQrCodeNotUpdateAllowed(DefaultQrCodeNotUpdatedException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(),ex.getError().getResponseCode());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxQrLimitExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxQrLimitExceeded(MaxQrLimitExceededException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(),ex.getError().getResponseCode());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
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
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.toList());

		String errorMessage = String.join(", ", messages);

		ErrorResponse error = new ErrorResponse(errorMessage, LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value());
		System.out.println(errorMessage);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<ErrorResponse> handleExternalServerErrors(ExternalServiceException ex) {
		String combined = ex.getMessage();
		String[] parts = combined.split("::", 2);

		String code = parts.length > 0 ? parts[0] : "UNKNOWN";
		String message = parts.length > 1 ? parts[1] : "No message provided";
        int errorCode = Integer.parseInt(code);
		ErrorResponse error = new ErrorResponse(message, LocalDateTime.now(),errorCode);
		return new ResponseEntity<>(error, HttpStatus.OK);
}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceErrors(DuplicateResourceException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(), ex.getError().getResponseCode());
		return new ResponseEntity<>(error, HttpStatus.OK);
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

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException exception) {
		ErrorResponse response = new ErrorResponse();
		response.setMessage("Order Not Found"); // keep message clean
		response.setTimestamp(LocalDateTime.now()); // set actual timestamp
		response.setResponseCode(400); // HTTP 400

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(),LocalDateTime.now(),HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(),LocalDateTime.now(),HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
	}

}
