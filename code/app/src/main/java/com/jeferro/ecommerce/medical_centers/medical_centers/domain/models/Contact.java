package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class Contact extends ValueObject {

  private static final int MAX_NAME_LENGTH = 100;
  private static final String EMAIL_REGEX =
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  private final String name;
  private final String nif;
  private final String phone;
  private final String email;
  private final String ppsPartialAdminUserCode;

  public Contact(String name,
                 String nif,
                 String phone,
                 String email,
                 String ppsPartialAdminUserCode) {
    if (name != null) {
      ValueValidator.ensure(
          () -> name.length() <= MAX_NAME_LENGTH,
          "name exceeds maximum length of " + MAX_NAME_LENGTH);
      ValueValidator.ensure(
          () -> !name.contains("\n"),
          "name must be single line");
    }

    if (email != null) {
      ensureEmailFormat(email);
    }

    this.name = name;
    this.nif = nif;
    this.phone = phone;
    this.email = email;
    this.ppsPartialAdminUserCode = ppsPartialAdminUserCode;
  }

  private void ensureEmailFormat(String email) {
    var matcher = EMAIL_PATTERN.matcher(email);

    if (!matcher.matches()) {
      throw ValueValidationException.createOfMessage("Incorrect email format: " + email);
    }
  }
}

