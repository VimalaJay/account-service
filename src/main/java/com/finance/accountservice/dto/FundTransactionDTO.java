package com.finance.accountservice.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class FundTransactionDTO {
	
	private Long transactionId;
	private String transactionStatus;
	private String transactionType;
	private String accountId;
	private Double amount;
	private Instant createdDate;
	private Instant updatedDate;

}
