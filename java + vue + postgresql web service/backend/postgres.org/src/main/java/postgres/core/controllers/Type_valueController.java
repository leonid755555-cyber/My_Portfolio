package  postgres.core.controllers;

import  postgres.core.dto.Type_valueDTO;
import  postgres.core.entities.Type_value;
import postgres.core.services.TypePropertiesService;
import  postgres.core.services.Type_valueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/type_value")
public class Type_valueController {
    private final Type_valueService serv;

    @GetMapping
    public Page<Type_valueDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }

    @PostMapping
    public Type_value save(@RequestBody Type_value type_value) {
        return serv.save(type_value);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
