package com.jeferro.shared.ddd.domain.models.aggregates;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

public abstract class UUID extends StringIdentifier {

  public UUID(String value) {
    super(value);
  }

  protected static String generateUuid() {
    NoArgGenerator timeBasedGenerator = Generators.timeBasedEpochGenerator();

    return timeBasedGenerator.generate().toString();
  }
}
