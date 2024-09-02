package com.finance.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.accountservice.util.Response;

@RestControllerAdvice
public class AccountExceptionHandler {

	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Response> customerHandler(ServiceException ex) {
		Response errorResponse = Response.builder().message(ex.getMessage()).statusCode(ex.getErrorCode()).build();
		return ResponseEntity.ok(errorResponse);
	}

	@ExceptionHandler(value = AccountAlreadyExistsException.class)
	public ResponseEntity<Response> accountExistsErrorhandler(Exception ex) {
		Response errorResponse = Response.builder().message(ex.getMessage()).statusCode(HttpStatus.CONFLICT.value())
				.build();
		return ResponseEntity.ok(errorResponse);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Response> handler(Exception ex) {
		Response errorResponse = Response.builder().message(ex.getMessage())
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
		return ResponseEntity.ok(errorResponse);
	}

}
