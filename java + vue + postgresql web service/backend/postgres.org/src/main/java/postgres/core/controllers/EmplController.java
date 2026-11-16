package postgres.core.controllers;

import postgres.core.dto.EmplDTO;
import postgres.core.dto.queries.DriverListDTO;
import postgres.core.dto.queries.PostInfoDTO;
import postgres.core.dto.queries.SubordinatesDTO;
import postgres.core.entities.Empl;
import postgres.core.services.EmplService;
import postgres.core.services.TypePropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/empl")
public class EmplController {
    private final EmplService serv;

    @Transactional
    @GetMapping
    public Page<EmplDTO> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return serv.getAll(offset, 10);
    }


    @GetMapping(path = "/driver_list")
    public List<DriverListDTO> getDriverList(@RequestParam(required = false) Integer auto) {
        return serv.getDriverList(auto);
    }

    @GetMapping(path = "/post_info")
    public List<PostInfoDTO> getPostInfo() {
        return serv.getPostInfo();
    }

    @GetMapping(path = "/subordinates")
    public List<SubordinatesDTO> getSubordinates(@RequestParam(required = false) Integer person_id) {
        return serv.getSubordinates(person_id);
    }

    @PostMapping
    public Empl save(@RequestBody EmplDTO emplDTO) {
        return serv.save(emplDTO);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@RequestParam(required = true) @PathVariable("id") Integer id) {
        serv.delete(id);
    }
}
