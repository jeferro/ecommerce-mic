package com.jeferro.shared.mappers.others;

import com.jeferro.shared.ddd.domain.models.value_objects.SimpleValueObject;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

@Mapper(componentModel = "spring")
public interface ValueObjectMapper {

  static <V extends SimpleValueObject<T>, T extends Serializable> T toDTO(V valueObject) {
    return valueObject == null ? null : valueObject.getValue();
  }

  static <V extends SimpleValueObject<?>, T> V toDomain(
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
}
