package postgres.core.mapper;

import postgres.core.dto.TypePropertiesDTO;
import postgres.core.entities.TypeProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class TypePropertiesMapper implements BaseMapper<TypeProperties, TypePropertiesDTO> {
    @Override
    @Mapping(source = "type.id", target = "typeId")
    public abstract TypePropertiesDTO entityToDto(TypeProperties typeProperties);

    @Mapping(source = "typeId", target = "type.id")
    public abstract TypeProperties dtoToEntity(TypePropertiesDTO typePropertiesDTO);
}
