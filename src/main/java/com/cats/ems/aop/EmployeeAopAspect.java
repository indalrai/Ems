package com.cats.ems.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.cats.ems.model.CredentialManager;
import com.cats.ems.model.Employee;

@Aspect
@Component
public class EmployeeAopAspect {
 
	private static final Logger LOGGER = LogManager.getLogger(EmployeeAopAspect.class);
	@Pointcut("execution(* com.cats.ems.controller.*.*(..))")
	public void loggingPointCut() {
	}

	@Around("loggingPointCut()")
	public Object around(ProceedingJoinPoint jointPoint) throws Throwable {
		LOGGER.info("Before method invoked..." + jointPoint.getSignature());
		Object object = jointPoint.proceed(); 

		if (object instanceof Employee) {
			LOGGER.info("After method invoked..." + jointPoint.getSignature());
		} else if (object instanceof CredentialManager) {
			LOGGER.info("After method invoked..." + jointPoint.getSignature());
		}
		return object;
	}

	@Around("@annotation(com.cats.ems.advice.TrackExecutionTime)")
	public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object obj = joinPoint.proceed();
		long endTime = System.currentTimeMillis();
		LOGGER.info("Method name... " + joinPoint.getSignature() + " Time taken to execute..." + (endTime - startTime));
		return obj;
	}
}
