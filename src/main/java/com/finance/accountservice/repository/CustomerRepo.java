package com.finance.accountservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.accountservice.Entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String> {
	
	public Optional<Customer> findByCustomerId(String customerId);

}
