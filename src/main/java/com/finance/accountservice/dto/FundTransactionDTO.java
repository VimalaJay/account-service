package com.finance.accountservice.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class FundTransactionDTO {
	
	private Long transactionId;
	private String transactionStatus;
	private String transactionType;
	private String accountId;
	private Double amount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
	private Instant createdDate;

}
