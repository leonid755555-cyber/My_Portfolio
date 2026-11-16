package postgres.core.mapper;

import postgres.core.dto.EmplDTO;
import postgres.core.dto.TransportDTO;
import postgres.core.entities.Empl;
import postgres.core.entities.Transport;
import postgres.core.services.PlacesService;
import postgres.core.services.PostsService;
import postgres.core.services.TypesService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TypesService.class, PlacesService.class})
public abstract class TransportMapper implements BaseMapper<Transport, TransportDTO> {
    @Override
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.name", target = "typeName")
    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "place.name", target = "placeName")
    public abstract TransportDTO entityToDto(Transport transport);
    
    @Override
    @Mapping(source = "placeId", target = "place")
    @Mapping(source = "typeId", target = "type")
    public abstract Transport dtoToEntity(TransportDTO TransportDTO);
}
