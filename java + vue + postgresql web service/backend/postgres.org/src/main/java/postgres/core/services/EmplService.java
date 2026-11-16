package postgres.core.services;

import postgres.core.dto.EmplDTO;
import postgres.core.dto.queries.DriverListDTO;
import postgres.core.dto.queries.PostInfoDTO;
import postgres.core.dto.queries.SubordinatesDTO;
import postgres.core.entities.Empl;
import postgres.core.entities.Transport;
import postgres.core.mapper.EmplMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.EmplRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmplService {
    private final EmplRepository repo;

    private final EmplMapper emplMapper;

    public Page<EmplDTO> getAll(Integer offset, Integer limit) {
        Page<Empl> empl = repo.findAll(PageRequest.of(offset, limit));
        return emplMapper.entitiesToDtos(empl);
    }

    public List<DriverListDTO> getDriverList(Integer auto) {
        return repo.getDriverList(auto);
    }

    public List<PostInfoDTO> getPostInfo() {
        return repo.getPostInfo();
    }

    public List<SubordinatesDTO> getSubordinates(Integer person_id) {
        return repo.getSubordinates(person_id);
    }
    
    
    public Empl save(EmplDTO emplDTO) {
        return repo.save(emplMapper.dtoToEntity(emplDTO));
    }

    public Empl findById(Integer id) {
        return repo.getById(id);
    }
    
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
