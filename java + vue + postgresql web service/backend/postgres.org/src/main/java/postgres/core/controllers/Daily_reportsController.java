package postgres.core.controllers;

import postgres.core.dto.Daily_reportsDTO;
import postgres.core.dto.queries.MileageDTO;
import postgres.core.dto.queries.CargoTransportationDTO;
import postgres.core.entities.Daily_reports;
import postgres.core.services.Daily_reportsService;
import postgres.core.services.EmplService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/daily_reports")
public class Daily_reportsController {
    private final Daily_reportsService serv;

    @GetMapping
    public Page<Daily_reportsDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @GetMapping(path = "/mileage")
    public List<MileageDTO> getMileageDTO(@RequestParam(required = false) Integer auto_Id,
                                          @RequestParam(required = false) String typeName,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        return serv.getMileageDTO(auto_Id,
                                  typeName,
                                  startDate,
                                  endDate);
    }

    @GetMapping(path = "/cargo_transportation")
    public List<CargoTransportationDTO> getCargoTransportationDTO(@RequestParam(required = false) Integer auto_Id,
                                                                  @RequestParam(required = false) String typeName,
                                                                  @RequestParam(required = false) String startDate,
                                                                  @RequestParam(required = false) String endDate) {
        return serv.getCargoTransportationDTO(auto_Id,
                                              typeName,
                                              startDate,
                                              endDate);
    }

    @PostMapping
    public Daily_reports save(@RequestBody Daily_reportsDTO daily_reportsDTO) {
        return serv.save(daily_reportsDTO);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
