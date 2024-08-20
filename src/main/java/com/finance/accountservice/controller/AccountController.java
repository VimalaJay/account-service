package com.finance.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.finance.accountservice.dto.AccountDTO;
import com.finance.accountservice.service.AccountService;
import com.finance.accountservice.util.Response;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/v1/accounts")
@Validated
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@GetMapping("/")
	public String index() {
		return "home.html";
	}

	@GetMapping("/createAccount")
	public ResponseEntity<Response> createAccount(@NotBlank @RequestParam String customerId,
			@RequestParam(value = "initialCredit", required = false) Double initialCredit) throws Exception {
		Response response = accountService.createAccount(customerId, initialCredit);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/customer/{customerId}")
	public List<AccountDTO> getCustomerAccounts(@PathVariable String customerId) {
		return accountService.getAccountsByCustomerId(customerId);
	}

}
