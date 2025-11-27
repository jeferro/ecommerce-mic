package com.jeferro.shared.ddd.infrastructure.rest_api.mappers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectRestMapper {

  @Around("execution(* ..rest_api.mappers..*(..))")
  public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
	try {
	  return joinPoint.proceed();

	} catch (Exception cause) {
	  throw new HttpMessageNotReadableException(cause.getMessage(), cause);
	}
  }
}
