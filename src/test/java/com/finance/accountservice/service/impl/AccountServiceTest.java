package com.finance.accountservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.finance.accountservice.dto.AccountDTO;
import com.finance.accountservice.exceptions.ServiceException;
import com.finance.accountservice.external.service.TransactionRestService;
import com.finance.accountservice.repository.AccountRepo;
import com.finance.accountservice.repository.CustomerRepo;
import com.finance.accountservice.util.Response;
import com.finance.accountservice.util.ServiceUtil;
import com.finance.accountservice.util.TestUtil;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
	private AccountRepo accountRepo;

	@Mock
	private CustomerRepo customerRepo;

	@Mock
	private TransactionRestService transactionService;

	@Mock
	ServiceUtil util;

	TestUtil testUtil = new TestUtil();

	@BeforeEach
	public void setup() {
		Mockito.when(customerRepo.findByCustomerId("56978545")).thenReturn(testUtil.getCustomer());
	}

	@Test
	public void createAccount_Without_intitalCredit() throws Exception {
		Mockito.when(util.generateAccNo()).thenReturn("25698746");
		Response response = accountService.createAccount("56978545", 0.0);
		assertThat(response.getHttpStatus().equals(HttpStatus.CREATED)).isTrue();
	}

	@Test
	public void createAccount_With_intitalCredit() throws Exception {
		Mockito.when(util.generateAccNo()).thenReturn("25698746");
		Mockito.when(transactionService.createTransaction(Mockito.anyString(), Mockito.anyDouble())).thenReturn(new Response("Success", HttpStatus.OK));
		Response response = accountService.createAccount("56978545", 1000.0);
		assertThat(response.getHttpStatus().equals(HttpStatus.CREATED)).isTrue();
	}
	
	@Test
	public void createAccount_Failure() throws Exception {
		Mockito.when(customerRepo.findByCustomerId("56978545")).thenReturn(Optional.empty());
		assertThatThrownBy(() -> accountService.createAccount("56978545", 1000.0)).isInstanceOf(ServiceException.class);
	}

	@Test
	public void fetch_Transactions_Success() {
		Mockito.when(accountRepo.findByCustomer(Mockito.any())).thenReturn(testUtil.getAccountsForCustomer());
		Mockito.when(transactionService.getTransactions(Mockito.anyString())).thenReturn(testUtil.getTransactionForAccount());
		List<AccountDTO> accountResponse = accountService.getAccountsByCustomerId("56978545");
		assertThat(accountResponse.get(0).getTransactions()).isNotEmpty();
	}
	
	@Test
	public void fetch_Empty_Transactions() {
		Mockito.when(accountRepo.findByCustomer(Mockito.any())).thenReturn(testUtil.getAccountsForCustomer());
		List<AccountDTO> accountResponse = accountService.getAccountsByCustomerId("56978545");
		assertThat(accountResponse.get(0).getTransactions()).isEmpty();
	}

}
