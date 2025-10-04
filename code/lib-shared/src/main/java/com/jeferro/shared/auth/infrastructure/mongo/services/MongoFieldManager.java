package com.jeferro.shared.auth.infrastructure.mongo.services;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

@Component
public class MongoFieldManager {

  public static final String ID = "id";

  public String getFieldIdValue(Object document) {
    return getFieldIdValue(document, document.getClass());
  }

  private String getFieldIdValue(Object document, Class<?> documentClass) {
    Field fieldId =
        Arrays.stream(documentClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Id.class) || field.getName().equals(ID))
            .findFirst()
            .orElse(null);

    if (fieldId != null) {
      return getFieldValue(fieldId, document).toString();
    }

    Class<?> parentClass = documentClass.getSuperclass();

    if (!parentClass.equals(Object.class)) {
      return getFieldIdValue(document, parentClass);
    }

    throw new RuntimeException("Document hasn't id field " + document);
  }

  public Map<String, Object> calculateFieldToUpdate(Object document) {
    return calculateFieldToUpdate(document, document.getClass());
  }

  public Map<String, Object> calculateFieldToUpdate(Object document, Class<?> documentClass) {
    final Map<String, Object> fields = new HashMap<>();

    Arrays.stream(documentClass.getDeclaredFields())
        .filter(field -> !field.isAnnotationPresent(Transient.class))
        .forEach(
            field -> {
              final String fieldName = getFieldName(field);
              final Object fieldValue = getFieldValue(field, document);

              fields.put(fieldName, fieldValue);
            });

    Class<?> parentClass = documentClass.getSuperclass();

    if (!parentClass.equals(Object.class)) {
      final Map<String, Object> parentFields = calculateFieldToUpdate(document, parentClass);

      fields.putAll(parentFields);
    }

    return fields;
  }

  private String getFieldName(java.lang.reflect.Field field) {
    if (field.isAnnotationPresent(org.springframework.data.mongodb.core.mapping.Field.class)) {
      org.springframework.data.mongodb.core.mapping.Field mongoField =
          field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
      return mongoField.name();
    }
    return field.getName();
  }

  private Object getFieldValue(Field field, Object document) {
    try {
      field.setAccessible(true);
      return field.get(document);
    } catch (IllegalAccessException cause) {
      throw new RuntimeException("Access denied to the field: " + field.getName(), cause);
    }
  }
}
