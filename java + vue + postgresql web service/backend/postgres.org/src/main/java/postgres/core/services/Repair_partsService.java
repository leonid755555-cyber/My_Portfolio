package postgres.core.services;

import postgres.core.dto.Repair_partsDTO;
import postgres.core.entities.Repair_parts;
import postgres.core.mapper.Repair_partsMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.Repair_partsRepository;
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
public class Repair_partsService {
    private final Repair_partsRepository repo;

    private final Repair_partsMapper repair_partsMapper;


    public Page<Repair_partsDTO> getAll(Integer offset, Integer limit) {
        Page<Repair_parts> repair_parts = repo.findAll(PageRequest.of(offset, limit));
        return repair_partsMapper.entitiesToDtos(repair_parts);
    }

    public Repair_parts save(Repair_parts repair_parts) {
        return repo.save(repair_parts);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
