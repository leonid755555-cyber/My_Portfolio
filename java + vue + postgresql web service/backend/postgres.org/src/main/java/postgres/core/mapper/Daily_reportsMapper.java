package postgres.core.mapper;

import postgres.core.dto.Daily_reportsDTO;
import postgres.core.dto.Repair_reportsDTO;
import postgres.core.entities.Daily_reports;
import postgres.core.entities.Repair_reports;
import postgres.core.services.PlacesService;
import postgres.core.services.TransportService;
import postgres.core.services.TypesService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TransportService.class)
public abstract class Daily_reportsMapper implements BaseMapper<Daily_reports, Daily_reportsDTO> {
    @Override
    @Mapping(source = "transport.id", target = "transportId")
    public abstract Daily_reportsDTO entityToDto(Daily_reports daily_reports);

    @Override
    @Mapping(source = "transportId", target = "transport")
    public abstract Daily_reports dtoToEntity(Daily_reportsDTO daily_reportsDTO);
}
