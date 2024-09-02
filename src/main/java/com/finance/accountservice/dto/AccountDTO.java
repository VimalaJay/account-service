package com.finance.accountservice.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AccountDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8887372389750785064L;
	private String accountNo;
	private String firstName;
	private String surName;
	private Double balance;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
	private Instant createdDate;
	List<FundTransactionDTO> transactions;

}
