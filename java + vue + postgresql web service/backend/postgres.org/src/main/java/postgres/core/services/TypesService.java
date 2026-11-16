package postgres.core.services;

import postgres.core.entities.Posts;
import postgres.core.entities.Types;
import postgres.core.repositories.TypesRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypesService{
    private final TypesRepository repo;

    public Page<Types> getAll(Integer offset, Integer limit) {
        return repo.findAll(PageRequest.of(offset, limit));
    }
    
    public Types getType(int id) {
    	return repo.findById(id).get();
    }

    public Types findById(Integer id) {
        return repo.getById(id);
    }
    
    public Types save(Types types) {
        return repo.save(types);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }

}

