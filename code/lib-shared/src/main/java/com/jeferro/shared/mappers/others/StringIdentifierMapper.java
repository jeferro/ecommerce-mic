package com.jeferro.shared.mappers.others;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import java.lang.reflect.InvocationTargetException;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

@Mapper
public interface StringIdentifierMapper {

  static <V extends StringIdentifier> String toDTO(V valueObject) {
    return valueObject == null ? null : valueObject.toString();
  }

  static <V extends StringIdentifier> V toDomain(
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
