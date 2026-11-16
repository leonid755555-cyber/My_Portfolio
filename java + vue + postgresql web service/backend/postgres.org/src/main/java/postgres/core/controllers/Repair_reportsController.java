package postgres.core.controllers;

import postgres.core.dto.Repair_reportsDTO;
import postgres.core.dto.queries.RepairCountAndCostDTO;
import postgres.core.dto.queries.RepairPartsInfoDTO;
import postgres.core.dto.queries.RepairInfoDTO;
import postgres.core.entities.Repair_reports;
import postgres.core.services.Repair_reportsService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/repair_reports")
public class Repair_reportsController {
    private final Repair_reportsService serv;


    @GetMapping
    public Page<Repair_reportsDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @GetMapping(path = "/count_and_cost")
    public List<RepairCountAndCostDTO> getRepairCountAndCost(@RequestParam(required = false) Integer auto_Id,
                                                             @RequestParam(required = false) Integer typeName,
                                                             @RequestParam(required = false) LocalDate startDate,
                                                             @RequestParam(required = false) LocalDate endDate) {
        return serv.getRepairCountAndCost(auto_Id,
                                          typeName,
                                          startDate,
                                          endDate);
    }

    @GetMapping(path = "/parts_info")
    public List<RepairPartsInfoDTO> getRepairPartsInfo(@RequestParam(required = false) Integer auto_Id,
                                                       @RequestParam(required = false) Integer tName,
                                                       @RequestParam(required = false) Integer partsList,
                                                       @RequestParam(required = false) LocalDate startDate,
                                                       @RequestParam(required = false) LocalDate endDate) {
        return serv.getRepairPartsInfo(auto_Id,
                                       tName,
                                       partsList,
                                       startDate,
                                       endDate);
    }

    @GetMapping(path = "/info")
    public List<RepairInfoDTO> getRepairInfo(@RequestParam(required = false) Integer auto_Id,
                                             @RequestParam(required = false) Integer tName,
                                             @RequestParam(required = false) Integer partsList,
                                             @RequestParam(required = false) LocalDate startDate,
                                             @RequestParam(required = false) LocalDate endDate) {
        return serv.getRepairInfo(auto_Id,
                                  tName,
                                  startDate,
                                  endDate);
    }

    @PostMapping
    public Repair_reports save(@RequestBody Repair_reportsDTO repair_reportsDTO) {
        return serv.save(repair_reportsDTO);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
