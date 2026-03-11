package com.jeferro.shared.time.domain.services;

import java.time.Instant;

public class InstantTimeService implements TimeGenerator {

  @Override
  public Instant generate() {
    return Instant.now();
  }
}
