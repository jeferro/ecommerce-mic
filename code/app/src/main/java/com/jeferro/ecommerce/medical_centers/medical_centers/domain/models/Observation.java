package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class Observation extends ValueObject {

  private static final int MAX_PUBLISHABLE_TEXT_LENGTH = 200;
  private static final int MAX_NON_PUBLISHABLE_TEXT_LENGTH = 400;

  private final boolean publishable;
  private final String text;

  public Observation(boolean publishable, String text) {
    ValueValidator.ensureNotNull(text, "text");

    var maxLength = publishable ? MAX_PUBLISHABLE_TEXT_LENGTH : MAX_NON_PUBLISHABLE_TEXT_LENGTH;
    ValueValidator.ensure(
        () -> text.length() <= maxLength,
        "text exceeds maximum length of " + maxLength + " for publishable=" + publishable);

    this.publishable = publishable;
    this.text = text;
  }
}

