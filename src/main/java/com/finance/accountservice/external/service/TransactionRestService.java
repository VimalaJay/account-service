package com.finance.accountservice.external.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.accountservice.dto.FundTransactionDTO;
import com.finance.accountservice.exceptions.ServiceException;
import com.finance.accountservice.util.Response;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionRestService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${transaction.service.url}")
	private String transactionServiceURL;

	@CircuitBreaker(name = "create-account", fallbackMethod = "getTransactionFallback")
	public List<FundTransactionDTO> getTransactions(String accountId) {

		List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		log.info("Invoke transaction service call to fetch transaction");
		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.exchange(transactionServiceURL + "/transaction/" + accountId,
					HttpMethod.GET, entity, Object.class);
		} catch (Throwable throwable) {
			log.error("Transaction service is down");
			throw new ServiceException("Transaction service is down.");
		}
		log.info("transaction service call successfully completed");
		
		ObjectMapper mapper = new ObjectMapper();
		List<FundTransactionDTO> result = mapper.convertValue(response.getBody(),
				new com.fasterxml.jackson.core.type.TypeReference<List<FundTransactionDTO>>() {
				});
		return result;

	}

	public List<FundTransactionDTO> getTransactionFallback(Throwable throwable) {
    		log.info("Into create account fallback method");
    		return new ArrayList<FundTransactionDTO>();
   	 }

	@CircuitBreaker(name = "transaction", fallbackMethod = "creatTransactionFallback")
	public Response createTransaction(String accountId, Double amount) {

		List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		FundTransactionDTO fundTransactionDTO = new FundTransactionDTO();
		fundTransactionDTO.setAccountId(accountId);
		fundTransactionDTO.setAmount(amount);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<FundTransactionDTO> request = new HttpEntity<>(fundTransactionDTO, headers);

		String url = transactionServiceURL + "/add?accountId=" + accountId + "&amount=" + amount;

		log.info("Invoke transaction service call to create transaction");
		try {
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
		} catch (Throwable throwable) {
			log.error("Transaction service call failed");
			throw new ServiceException("Transaction service call failed");
		}
		log.info("Transaction service call successfully completed");

		return new Response("Transaction created", HttpStatus.OK);

	}

	public Response creatTransactionFallback(Throwable throwable) {
		log.info("Into fetch transaction fallback method");
		return new Response("Failure", HttpStatus.OK);
	}

}
