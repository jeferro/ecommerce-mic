package com.jeferro.shared.auth.infrastructure.rest.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("shared.auth.rest")
public record RestSecurityProperties(
	String issuer,
	Duration duration,
	String password) {

  public long durationAsMillis() {
	return duration.toMillis();
  }

  public boolean hasDuration() {
	return duration != null;
  }
}
