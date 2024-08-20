package com.finance.accountservice.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1310465043435526188L;
	private String message;
	private HttpStatus errorCode;

	public ServiceException(String message) {
		super(message);
	}

}
