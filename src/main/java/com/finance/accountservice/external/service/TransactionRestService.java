package com.finance.accountservice.external.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.accountservice.dto.FundTransactionDTO;

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

		ResponseEntity<Object> response = restTemplate.exchange(transactionServiceURL + "/transaction/" + accountId,
				HttpMethod.GET, entity, Object.class);
		
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
	public void createTransaction(String accountId, Double amount) {

		List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		FundTransactionDTO fundTransactionDTO = new FundTransactionDTO();
		fundTransactionDTO.setAccountId(accountId);
		fundTransactionDTO.setAmount(amount);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<FundTransactionDTO> request = new HttpEntity<>(fundTransactionDTO, headers);

		String url = transactionServiceURL + "/add?accountId=" + accountId + "&amount=" + amount;

		ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
		System.out.println("test");

	}

	public Response creatTransactionFallback(Throwable throwable) {
		log.info("Into fetch transaction fallback method");
		return new Response("Failure", HttpStatus.OK);
	}

}
