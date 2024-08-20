package com.finance.accountservice.Entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Customer {
	
	@Id
	private String customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	
	@OneToMany(mappedBy="customer", fetch = FetchType.EAGER)
    private Set<Account> accounts;
	
}
