package com.finance.accountservice.configuration;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {
	
//	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	 return builder
	  .setConnectTimeout(Duration.ofMillis(60000))
	  .setReadTimeout(Duration.ofMillis(60000))
	  .build();
	}

}
