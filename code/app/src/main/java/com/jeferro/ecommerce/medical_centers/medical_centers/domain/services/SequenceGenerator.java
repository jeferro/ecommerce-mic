package com.jeferro.ecommerce.medical_centers.medical_centers.domain.services;

public interface SequenceGenerator {

  <T> String generate(Class<T> entityClass);
}

