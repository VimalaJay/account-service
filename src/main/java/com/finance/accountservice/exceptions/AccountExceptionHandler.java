package com.finance.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.accountservice.util.Response;

@RestControllerAdvice
public class AccountExceptionHandler {
	
	@ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Response> customerHandler(
			ServiceException ex) {
		Response errorResponse = Response.builder().message(ex.getMessage()).httpStatus(ex.getErrorCode())
				.build();
		return ResponseEntity.ok(errorResponse);
	}
	
	@ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response> handler(
			Exception ex) {
		Response errorResponse = Response.builder().message(ex.getMessage()).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();
		return ResponseEntity.ok(errorResponse);
	}

}
