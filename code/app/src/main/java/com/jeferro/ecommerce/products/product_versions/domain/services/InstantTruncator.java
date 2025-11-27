package com.jeferro.ecommerce.products.product_versions.domain.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class InstantTruncator {

  public static Instant trunkToSeconds(Instant date) {
    return date != null ? date.truncatedTo(ChronoUnit.SECONDS) : null;
  }
}
