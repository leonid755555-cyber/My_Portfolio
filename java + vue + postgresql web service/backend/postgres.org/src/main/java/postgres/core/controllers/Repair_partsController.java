package postgres.core.controllers;

import postgres.core.dto.Repair_partsDTO;
import postgres.core.entities.Repair_parts;
import postgres.core.services.Repair_partsService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/repair_parts")
public class Repair_partsController {
    private final Repair_partsService serv;


    @GetMapping
    public Page<Repair_partsDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Repair_parts save(@RequestBody Repair_parts repair_parts) {
        return serv.save(repair_parts);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
