package postgres.core.controllers;

import postgres.core.dto.queries.PlaceInfoDTO;
import postgres.core.entities.Places;
import postgres.core.services.PlacesService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/places")
public class PlacesController {
    private final PlacesService serv;


    @GetMapping
    public Page<Places> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Places save(@RequestBody Places places) {
        return serv.save(places);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
