package postgres.core.controllers;

import postgres.core.dto.Transport_emplDTO;
import postgres.core.entities.Transport_empl;
import postgres.core.services.Transport_emplService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/transport_empl")
public class Transport_emplController {
    private final Transport_emplService serv;


    @GetMapping
    public Page<Transport_emplDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Transport_empl save(@RequestBody Transport_empl transport_empl) {
        return serv.save(transport_empl);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
