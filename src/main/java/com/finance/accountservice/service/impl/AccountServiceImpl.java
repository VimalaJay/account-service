package com.finance.accountservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.finance.accountservice.Entity.Account;
import com.finance.accountservice.Entity.Customer;
import com.finance.accountservice.dto.AccountDTO;
import com.finance.accountservice.dto.FundTransactionDTO;
import com.finance.accountservice.exceptions.ServiceException;
import com.finance.accountservice.external.service.TransactionRestService;
import com.finance.accountservice.repository.AccountRepo;
import com.finance.accountservice.repository.CustomerRepo;
import com.finance.accountservice.service.AccountService;
import com.finance.accountservice.util.AccountType;
import com.finance.accountservice.util.Response;
import com.finance.accountservice.util.ServiceUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	ServiceUtil util;
	
	@Autowired
	TransactionRestService transactionService;
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public Response createAccount(String customerId, Double initialCredit) throws Exception {
		
		log.info("Create new current account for customer");
		Optional<Customer> customerEntity = customerRepo.findByCustomerId(customerId);
		
		if (!customerEntity.isPresent()) {
			throw new ServiceException("Customer not found!", HttpStatus.NOT_FOUND);
		}
		
		Customer customer = customerEntity.get();
		
		Account account = new Account();
		account.setCustomer(customer);
		account.setAccountNo(util.generateAccNo());
		account.setAccountName(customer.getFirstName() + " " + customer.getLastName());
		account.setAccountType(AccountType.CURRENT.name());

		String responseMsg = "Account and credit transaction created successfully!";
		try {
			log.info("Persist account and transaction data to DB");
			if (initialCredit != null && initialCredit > 0) {
				Response response = transactionService.createTransaction(account.getAccountNo(), initialCredit);

				if (response.getMessage().equals("Failure")) {
					responseMsg = "Account Created but Credit Transaction Failed!";
				}
			}
			accountRepo.save(account);
		} catch (Exception e) {
			throw new ServiceException("Error occured while creating account!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully created current account");
		Response response = new Response(responseMsg, HttpStatus.CREATED);
		return response;
	}

	@Override
	public List<AccountDTO> getAccountsByCustomerId(String customerId) {
		log.info("Start processing to fetch transaction details");
		Optional<Customer> customerEntity = customerRepo.findByCustomerId(customerId);
		
		Set<Account> accounts = accountRepo.findByCustomer(customerEntity.get());
		if (accounts.isEmpty()) {
			throw new ServiceException("Account Not Exists", HttpStatus.NO_CONTENT);
		}
		
		List<AccountDTO> accountDtos = new ArrayList<>();
		accounts.forEach(account -> {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setFirstName(customerEntity.get().getFirstName());
			accountDTO.setSurName(customerEntity.get().getLastName());
			
			List<FundTransactionDTO> transactions = new ArrayList<>();
			try { 
				transactions = transactionService.getTransactions(account.getAccountNo());
			} catch (Exception e) {
				throw new ServiceException("Error occurred while ", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Double balanceAmt = transactions.stream().collect(Collectors.summingDouble(FundTransactionDTO::getAmount));
			accountDTO.setBalance(balanceAmt);
			accountDTO.setTransactions(transactions);
			
			accountDtos.add(accountDTO);
		});
		
		log.info("Successfully fetched transaction details");
		return accountDtos;
	}


}
