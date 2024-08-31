package com.finance.accountservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Around("execution(* com.finance.accountservice..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Entering Method: [" + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName() + "]");
		Object result;
		try {
			result = joinPoint.proceed();
			log.info("Exiting Method: [" + joinPoint.getSignature().getDeclaringTypeName() + "."
					+ joinPoint.getSignature().getName() + "]");
		} catch (Throwable error) {
			log.info("Exception in Method: [" + joinPoint.getSignature().getDeclaringTypeName() + "."
					+ joinPoint.getSignature().getName() + "]");
			throw error;
		}
		return result;
	}

}
