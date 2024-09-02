package com.finance.accountservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -3486215774998951732L;

	private String message;
	private int errorCode;

	public ServiceException(String message) {
		super(message);
	}

}
