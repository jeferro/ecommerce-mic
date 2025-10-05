package com.jeferro.shared.auth.infrastructure.mongo.services;

import java.util.Optional;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class MongoAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(ContextManager.getAuth().getUsername());
	}
}
