package com.tutorial.aspct;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
	private static final Logger LOG = Logger.getLogger(LoggingAspect.class);
	
	@Before("execution(* com.tutorial.service.UserService.add(..))")
	public void logBefore(JoinPoint joinPoint) {
		LOG.info("**************************************************");
		LOG.info("logBefore() is running!");
		LOG.info("Signature Name: " + joinPoint.getSignature().getName());
		LOG.info("**************************************************");
	}
}
