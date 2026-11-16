package postgres.core.controllers;

import postgres.core.dto.EmplDTO;
import postgres.core.entities.Types;
import postgres.core.services.TypesService;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/types")
public class TypesController {
    private final TypesService serv;

    @GetMapping
    public Page<Types> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Types save(@RequestBody Types types) {
        return serv.save(types);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        serv.delete(id);
    }
    
}
