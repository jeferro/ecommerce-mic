package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.Getter;

@Getter
public class BankAccount extends ValueObject {

  private final String iban;
  private final String swift;
  private final String accountHolder;

  public BankAccount(String iban, String swift, String accountHolder) {
    this.iban = iban;
    this.swift = swift;
    this.accountHolder = accountHolder;
  }
}

