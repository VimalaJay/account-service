package com.finance.accountservice.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {
	
	public String generateAccNo() {
		Random random = new Random();
		return String.valueOf(random.nextInt(10000000));
	}

}
