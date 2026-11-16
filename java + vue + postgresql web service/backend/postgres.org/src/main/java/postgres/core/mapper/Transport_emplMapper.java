package postgres.core.mapper;

import postgres.core.dto.Transport_emplDTO;
import postgres.core.entities.Transport_empl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class Transport_emplMapper implements BaseMapper<Transport_empl, Transport_emplDTO> {
    @Override
    @Mapping(source = "transport.id", target = "transportId")
    @Mapping(source = "empl.id", target = "emplId")
    public abstract Transport_emplDTO entityToDto(Transport_empl transport_empl);
}
