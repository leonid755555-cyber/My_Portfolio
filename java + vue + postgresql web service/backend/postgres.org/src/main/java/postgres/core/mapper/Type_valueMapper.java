package postgres.core.mapper;

import postgres.core.dto.Type_valueDTO;
import postgres.core.entities.Type_value;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class Type_valueMapper implements BaseMapper<Type_value, Type_valueDTO> {
    @Override
    @Mapping(source = "transport.id", target = "transportId")
    @Mapping(source = "prop.id", target = "propId")
    public abstract Type_valueDTO entityToDto(Type_value type_value);
}
