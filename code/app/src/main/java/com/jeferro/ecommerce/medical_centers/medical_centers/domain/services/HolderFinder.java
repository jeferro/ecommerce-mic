package com.jeferro.ecommerce.medical_centers.medical_centers.domain.services;

import java.time.LocalDate;

public interface HolderFinder {

  boolean exists(String holderId);

  LocalDate getRegistrationDate(String holderId);
}

