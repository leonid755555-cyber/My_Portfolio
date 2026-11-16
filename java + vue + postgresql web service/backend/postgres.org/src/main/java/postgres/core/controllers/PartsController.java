package postgres.core.controllers;

import postgres.core.entities.Parts;
import postgres.core.services.PartsService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/parts")
public class PartsController {
    private final PartsService serv;

    @GetMapping
    public Page<Parts> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Parts save(@RequestBody Parts parts) {
        return serv.save(parts);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
