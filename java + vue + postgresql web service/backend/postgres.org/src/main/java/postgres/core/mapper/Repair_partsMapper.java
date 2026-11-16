package postgres.core.mapper;

import postgres.core.dto.Repair_partsDTO;
import postgres.core.entities.Repair_parts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class Repair_partsMapper implements BaseMapper<Repair_parts, Repair_partsDTO> {
    @Override
    @Mapping(source = "repair_reports.id", target = "rrId")
    @Mapping(source = "part.id", target = "partId")
    public abstract Repair_partsDTO entityToDto(Repair_parts repair_parts);
}
