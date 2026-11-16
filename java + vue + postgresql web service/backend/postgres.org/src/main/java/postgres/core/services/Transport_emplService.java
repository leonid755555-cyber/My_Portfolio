package postgres.core.services;

import postgres.core.dto.Transport_emplDTO;
import postgres.core.entities.Transport_empl;
import postgres.core.mapper.Transport_emplMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.Transport_emplRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Transport_emplService {
    private final Transport_emplRepository repo;

    private final Transport_emplMapper transport_emplMapper;


    public Page<Transport_emplDTO> getAll(Integer offset, Integer limit) {
        Page<Transport_empl> transport_empl = repo.findAll(PageRequest.of(offset, limit));
        return transport_emplMapper.entitiesToDtos(transport_empl);
    }

    public Transport_empl save(Transport_empl transport_empl) {
        return repo.save(transport_empl);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
