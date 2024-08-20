package com.finance.accountservice.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.accountservice.Entity.Account;
import com.finance.accountservice.Entity.Customer;

public interface AccountRepo extends JpaRepository<Account, String> {

	Set<Account> findByCustomer(Customer customer);
	
}
