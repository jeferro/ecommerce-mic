package com.jeferro.shared.mappers;

import com.jeferro.shared.mappers.others.LocaleMapper;
import com.jeferro.shared.mappers.others.StringIdentifierMapper;
import com.jeferro.shared.mappers.others.ValueObjectMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;

@MapperConfig(
    uses = {ValueObjectMapper.class, LocaleMapper.class, StringIdentifierMapper.class},
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public class MapstructConfig {}
