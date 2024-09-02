package com.finance.accountservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finance.accountservice.Entity.Account;
import com.finance.accountservice.Entity.Customer;
import com.finance.accountservice.dto.AccountDTO;
import com.finance.accountservice.dto.FundTransactionDTO;
import com.finance.accountservice.exceptions.AccountAlreadyExistsException;
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
	public Response openAccount(String customerId, Double initialCredit) throws Exception {

		log.info("Create new current account for customer");
		Optional<Customer> customerEntity = customerRepo.findByCustomerId(customerId);

		if (!customerEntity.isPresent()) {
			throw new ServiceException("Customer not found!", HttpStatus.NOT_FOUND.value());
		}

		Customer customer = customerEntity.get();

		boolean flag = accountRepo.existsAccountByCustomerAndAccountType(customer, AccountType.CURRENT.name());
		if (flag) {
			throw new AccountAlreadyExistsException("Current account already exists!");
		}

		Account account = new Account();
		account.setCustomer(customer);
		account.setAccountNo(util.generateAccNo());
		account.setAccountName(customer.getFirstName() + " " + customer.getLastName());
		account.setAccountType(AccountType.CURRENT.name());

		String responseMsg = "Account opened successfully!";
		String transactionResMsg = null;
		try {
			log.info("Persist account and transaction data to DB");
			if (initialCredit != null && initialCredit > 0) {
				Response response = transactionService.createTransaction(account.getAccountNo(), initialCredit);
				transactionResMsg = " And Transaction successfully created!";
				if (response.getMessage().equals("Failure")) {
					transactionResMsg = " But Transaction Failed!";
				}
			}
			accountRepo.save(account);
		} catch (Exception e) {
			throw new ServiceException("Error occured while creating account!",
					HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		log.info("Successfully created current account");
		return new Response(responseMsg + transactionResMsg, HttpStatus.CREATED.value(), null);
	}

	@Override
	public Response getAccountsByCustomerId(String customerId) {
		log.info("Start processing to fetch transaction details");
		Optional<Customer> customerEntity = customerRepo.findByCustomerId(customerId);

		Set<Account> accounts = accountRepo.findByCustomer(customerEntity.get());
		if (accounts.isEmpty()) {
			throw new ServiceException("Account Not Exists", HttpStatus.NO_CONTENT.value());
		}

		List<AccountDTO> accountDtos = new ArrayList<>();
		accounts.forEach(account -> {
			accountDtos.add(buildAccountDTO(account));
		});

		log.info("Successfully fetched transaction details");
		return new Response("successfully fetched transaction data!", HttpStatus.OK.value(), accountDtos);
	}

	@Cacheable("accounts")
	@Override
	public Response getAccountDetails(String accountId) {

		Optional<Account> account = accountRepo.findById(accountId);

		return new Response("successfully fetched account details!", HttpStatus.OK.value(),
				buildAccountDTO(account.get()));
	}

	@CacheEvict(value = "accounts", allEntries = true)
	@Scheduled(fixedRateString = "${cache.ttl}")
	public void evictAllAccountsCache() {
		log.info("Evicted all the cache");
	}

	private AccountDTO buildAccountDTO(Account account) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountNo(account.getAccountNo());
		accountDTO.setFirstName(account.getCustomer().getFirstName());
		accountDTO.setSurName(account.getCustomer().getLastName());
		accountDTO.setCreatedDate(account.getCreatedDate());

		List<FundTransactionDTO> transactions = new ArrayList<>();
		try {
			log.info("fetch transaction data");
			transactions = transactionService.getTransactions(account.getAccountNo());
		} catch (Exception e) {
			throw new ServiceException("Error occurred while ", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		Double balanceAmt = transactions.stream().collect(Collectors.summingDouble(FundTransactionDTO::getAmount));
		accountDTO.setBalance(balanceAmt);
		accountDTO.setTransactions(transactions);

		return accountDTO;
	}

}
