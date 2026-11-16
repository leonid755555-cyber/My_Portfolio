package postgres.core.services;

import postgres.core.entities.Marshs;
import postgres.core.entities.Posts;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.MarshsRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarshsService {
    private final MarshsRepository repo;


    public Page<Marshs> getAll(Integer offset, Integer limit) {
        return repo.findAll(PageRequest.of(offset, limit));
    }

    public Marshs findById(Integer id) {
        return repo.getById(id);
    }
    
    public Marshs save(Marshs marshs) {
        return repo.save(marshs);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
