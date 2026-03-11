package com.jeferro.shared.mappers;

import com.jeferro.shared.mappers.others.CommonMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;

@MapperConfig(
        uses = {CommonMapper.class,},
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        componentModel = "spring")
public class MapstructConfig {
}
