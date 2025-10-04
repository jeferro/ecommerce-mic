package com.jeferro.shared.mappers.others;

import java.util.Locale;
import org.mapstruct.Mapper;

@Mapper
public class LocaleMapper {

  public Locale toDomain(String dto) {
    return Locale.forLanguageTag(dto);
  }

  public String toDTO(Locale locale) {
    return locale.toLanguageTag();
  }
}
