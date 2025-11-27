package com.jeferro.shared.mappers.others;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.value_objects.SimpleValueObject;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

@Mapper(componentModel = "spring")
public class CommonMapper {

  // Locale
  public Locale toDomain(String dto) {
    return Locale.forLanguageTag(dto);
  }

  public String toDTO(Locale locale) {
    return locale.toLanguageTag();
  }

  // SimpleValueObject
  public static <V extends SimpleValueObject<T>, T extends Serializable> T toDTO(V valueObject) {
    return valueObject == null ? null : valueObject.getValue();
  }

  public static <V extends SimpleValueObject<?>, T> V toDomain(
          T primitive, @TargetType Class<V> valueObjectClass) {
    if (primitive == null) {
      return null;
    }

    try {
      return valueObjectClass.getDeclaredConstructor(primitive.getClass()).newInstance(primitive);
    } catch (InstantiationException
             | IllegalAccessException
             | InvocationTargetException
             | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  // StringIdentifier
  public static <V extends StringIdentifier> String toDTO(V valueObject) {
    return valueObject == null ? null : valueObject.toString();
  }

  public static <V extends StringIdentifier> V toDomain(
          String primitive, @TargetType Class<V> valueObjectClass) {
    if (primitive == null) {
      return null;
    }

    try {
      return valueObjectClass.getDeclaredConstructor(primitive.getClass()).newInstance(primitive);
    } catch (InstantiationException
             | IllegalAccessException
             | InvocationTargetException
             | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
