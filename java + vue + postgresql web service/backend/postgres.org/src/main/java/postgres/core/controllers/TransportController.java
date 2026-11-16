package postgres.core.controllers;

import postgres.core.dto.TransportDTO;
import postgres.core.dto.queries.AutoListDTO;
import postgres.core.dto.queries.ScheduleDTO;
import postgres.core.dto.queries.sqlDTO;
import postgres.core.dto.queries.AutoPlaceInfoDTO;
import postgres.core.dto.queries.PurchasedDTO;
import postgres.core.dto.queries.DecommissionedDTO;
import postgres.core.dto.queries.PlaceInfoDTO;
import postgres.core.entities.Transport;
import postgres.core.services.TransportService;
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
@RequestMapping(path = "api/v1/transport")
public class TransportController {
    private final TransportService serv;


    @GetMapping
    public Page<TransportDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @GetMapping(path = "/list")
    public List<AutoListDTO> getAutoList() {
        return serv.getAutoList();
    }

    @GetMapping(path = "/sql")
    public int getSQL(String text) {
        return serv.getSQL(text);
    }

    @GetMapping(path = "/info")
    public List<PlaceInfoDTO> getPlaceInfo(@RequestParam(required = false) String tName) {
        return serv.getPlaceInfo(tName);
    }

    @GetMapping(path = "/place_info")
    public List<AutoPlaceInfoDTO> getAutoPlaceInfo() {
        return serv.getAutoPlaceInfo();
    }

    @GetMapping(path = "/decommissioned")
    public List<DecommissionedDTO> getDecommissioned(@RequestParam(required = false) LocalDate startDate,
                                                                 @RequestParam(required = false) LocalDate endDate) {
        return serv.getDecommissioned(startDate, endDate);
    }

    @GetMapping(path = "/purchased")
    public List<PurchasedDTO> getDecommissionedAndPurchased(@RequestParam(required = false) LocalDate startDate,
                                                                             @RequestParam(required = false) LocalDate endDate) {
        return serv.getPurchased(startDate, endDate);
    } 
    @PostMapping
    public Transport save(@RequestBody TransportDTO transportDTO) {
        return serv.save(transportDTO);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
