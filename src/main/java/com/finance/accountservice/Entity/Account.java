package com.finance.accountservice.Entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data public class Account implements Serializable {
	
	private static final long serialVersionUID = -2909497324318643297L;

	@Id
	private String accountNo;
	private String accountName;
	private String accountType;
//	private String customerId;
	private Instant createdDate = Instant.now();
	private Instant updatedDate = Instant.now();
	
	@ManyToOne
    @JoinColumn(name="customerId", nullable=false)
    private Customer customer;
	
}
