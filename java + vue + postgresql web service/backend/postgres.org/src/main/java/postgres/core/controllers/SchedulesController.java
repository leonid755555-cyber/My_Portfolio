package postgres.core.controllers;

import postgres.core.dto.SchedulesDTO;
import postgres.core.dto.queries.ScheduleDTO;
import postgres.core.entities.Schedules;
import postgres.core.services.SchedulesService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/schedules")
public class SchedulesController {
    private final SchedulesService serv;


    @GetMapping
    public Page<SchedulesDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @GetMapping(path = "/schedule")
    public List<ScheduleDTO> getSchedule() {
        return serv.getSchedule();
    }

    @PostMapping
    public Schedules save(@RequestBody Schedules schedules) {
        return serv.save(schedules);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
