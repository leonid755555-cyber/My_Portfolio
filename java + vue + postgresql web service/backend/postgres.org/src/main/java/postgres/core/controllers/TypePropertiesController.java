package postgres.core.controllers;

import postgres.core.dto.TypePropertiesDTO;
import postgres.core.entities.TypeProperties;
import postgres.core.services.TypePropertiesService;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/type_properties")
public class TypePropertiesController {
    private final TypePropertiesService serv;

    @GetMapping
    public Page<TypePropertiesDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public TypeProperties save(@RequestBody TypePropertiesDTO type_propertiesDTO) {
        return serv.save(type_propertiesDTO);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
