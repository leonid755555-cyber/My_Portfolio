package postgres.core.services;

import postgres.core.entities.Parts;
import postgres.core.entities.Posts;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.PartsRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PartsService {
    private final PartsRepository repo;


    public Page<Parts> getAll(Integer offset, Integer limit) {
        return repo.findAll(PageRequest.of(offset, limit));
    }

    public Parts findById(Integer id) {
        return repo.getById(id);
    }
    
    public Parts save(Parts parts) {
        return repo.save(parts);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
