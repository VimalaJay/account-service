package com.finance.accountservice.service;

import com.finance.accountservice.util.Response;

public interface AccountService {

	Response openAccount(String customerId, Double initialCredit) throws Exception;

	Response getAccountsByCustomerId(String customerId);

	Response getAccountDetails(String accountId);

}
