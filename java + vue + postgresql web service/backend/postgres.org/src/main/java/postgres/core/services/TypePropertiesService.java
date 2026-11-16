package postgres.core.services;

import postgres.core.dto.TypePropertiesDTO;
import postgres.core.entities.TypeProperties;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TypePropertiesService {
    private final TypePropertiesRepository repo;

    private final TypePropertiesMapper typePropertiesMapper;

    public Page<TypePropertiesDTO> getAll(Integer offset, Integer limit) {
        Page<TypeProperties> typeProperties = repo.findAll(PageRequest.of(offset, limit));
        return typePropertiesMapper.entitiesToDtos(typeProperties);
    }

    public TypeProperties save(TypePropertiesDTO typePropertiesDTO) {
        return repo.save(typePropertiesMapper.dtoToEntity(typePropertiesDTO));
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
