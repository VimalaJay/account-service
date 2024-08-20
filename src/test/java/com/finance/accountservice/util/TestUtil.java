package com.finance.accountservice.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.finance.accountservice.Entity.Account;
import com.finance.accountservice.Entity.Customer;
import com.finance.accountservice.dto.FundTransactionDTO;

@Component
public class TestUtil {
	
	public Optional<Customer> getCustomer() {
		Customer customer = new Customer();
		customer.setCustomerId("56978545");
		customer.setFirstName("Jhon");
		customer.setLastName("Henric");
		customer.setEmail("jhon.henric@xyz.com");
		customer.setMobile("789654698");
		return Optional.of(customer);
	}
	
	public Set<Account> getAccountsForCustomer() {
		Set<Account> accounts = new HashSet<>();
		
		Account account = new Account();
		account.setAccountNo("254698");
		account.setAccountName("Jhon Henric");
		account.setAccountType("CURRENT");
		account.setCreatedDate(Instant.now());
		account.setUpdatedDate(Instant.now());
		account.setCustomer(getCustomer().get());
		
		accounts.add(account);
		return accounts;
	}
	
	public List<FundTransactionDTO> getTransactionForAccount() {
		List<FundTransactionDTO> transactions = new ArrayList<>();
		
		FundTransactionDTO transaction = new FundTransactionDTO();
		transaction.setTransactionId(265896L);
		transaction.setTransactionType("CREDIT");
		transaction.setTransactionStatus("SUCCESS");
		transaction.setAmount(253.0);
		transaction.setCreatedDate(Instant.now());
		transaction.setUpdatedDate(Instant.now());
		
		
		transactions.add(transaction);
		return transactions;
	}
}
