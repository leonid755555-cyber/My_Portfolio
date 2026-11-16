package postgres.core.services;

import postgres.core.dto.SchedulesDTO;
import postgres.core.dto.queries.ScheduleDTO;
import postgres.core.entities.Schedules;
import postgres.core.mapper.SchedulesMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.SchedulesRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulesService {
    private final SchedulesRepository repo;

    private final SchedulesMapper schedulesMapper;

    public Page<SchedulesDTO> getAll(Integer offset, Integer limit) {
        Page<Schedules> schedules = repo.findAll(PageRequest.of(offset, limit));
        return schedulesMapper.entitiesToDtos(schedules);
    }   
    
    public List<ScheduleDTO> getSchedule() {
        return repo.getSchedule();
    }

    public Schedules save(Schedules schedules) {
        return repo.save(schedules);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
