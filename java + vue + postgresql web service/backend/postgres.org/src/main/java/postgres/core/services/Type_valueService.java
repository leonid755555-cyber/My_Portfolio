package postgres.core.services;

import postgres.core.dto.Type_valueDTO;
import postgres.core.entities.Type_value;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.mapper.Type_valueMapper;
import postgres.core.repositories.TypePropertiesRepository;
import postgres.core.repositories.Type_valueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Type_valueService {
    private final Type_valueRepository repo;

    private final Type_valueMapper type_valueMapper;
    public Page<Type_valueDTO> getAll(Integer offset, Integer limit) {
        Page<Type_value> type_value = repo.findAll(PageRequest.of(offset, limit));
        return type_valueMapper.entitiesToDtos(type_value);
    }

    public Type_value save(Type_value type_value) {
        return repo.save(type_value);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
