package com.finance.accountservice.service;

import java.util.List;

import com.finance.accountservice.dto.AccountDTO;
import com.finance.accountservice.util.Response;

public interface AccountService {
	
	Response createAccount(String customerId, Double initialCredit) throws Exception;
	
	List<AccountDTO> getAccountsByCustomerId(String customerId);

}
