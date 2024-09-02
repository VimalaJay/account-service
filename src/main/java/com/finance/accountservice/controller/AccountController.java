package com.finance.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finance.accountservice.service.AccountService;
import com.finance.accountservice.util.Response;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/v1/accounts")
@Validated
@CrossOrigin
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@GetMapping("/openAccount")
	public ResponseEntity<Response> openAccount(@NotBlank @RequestParam String customerId,
			@RequestParam(value = "initialCredit", required = false) Double initialCredit) throws Exception {
		Response response = accountService.openAccount(customerId, initialCredit);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{accountId}")
	public ResponseEntity<Response> getAccountDetails(@PathVariable String accountId) {
		return  ResponseEntity.ok().body(accountService.getAccountDetails(accountId));
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<Response> getCustomerAccounts(@PathVariable String customerId) {
		return ResponseEntity.ok().body(accountService.getAccountsByCustomerId(customerId));
	}
	
}
