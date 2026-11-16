package postgres.core.mapper;

import postgres.core.dto.SchedulesDTO;
import postgres.core.entities.Schedules;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SchedulesMapper implements BaseMapper<Schedules, SchedulesDTO> {
    @Override
    @Mapping(source = "transport.id", target = "transportId")
    @Mapping(source = "marshrut.id", target = "marshrutId")
    @Mapping(source = "marshrut.name", target = "marshrutName")
    public abstract SchedulesDTO entityToDto(Schedules schedules);
}
