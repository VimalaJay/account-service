package com.finance.accountservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AccountDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8887372389750785064L;
	private String firstName;
	private String surName;
	private Double balance;
	List<FundTransactionDTO> transactions;

}
