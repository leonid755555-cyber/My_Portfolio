package postgres.core.mapper;

import postgres.core.dto.Repair_reportsDTO;
import postgres.core.dto.TransportDTO;
import postgres.core.entities.Repair_reports;
import postgres.core.entities.Transport;
import postgres.core.services.EmplService;
import postgres.core.services.PlacesService;
import postgres.core.services.TransportService;
import postgres.core.services.TypesService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmplService.class, TransportService.class})
public abstract class Repair_reportsMapper implements BaseMapper<Repair_reports, Repair_reportsDTO> {
    @Override
    @Mapping(source = "transport.id", target = "transportId")
    @Mapping(source = "empl.id", target = "emplId")
    public abstract Repair_reportsDTO entityToDto(Repair_reports repair_reports);

    @Override
    @Mapping(source = "emplId", target = "empl")
    @Mapping(source = "transportId", target = "transport")
    public abstract Repair_reports dtoToEntity(Repair_reportsDTO repair_reportsDTO);
}
