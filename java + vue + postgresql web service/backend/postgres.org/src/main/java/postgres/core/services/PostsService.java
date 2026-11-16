package postgres.core.services;

import postgres.core.entities.Posts;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.PostsRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository repo;

    public Page<Posts> getAll(Integer offset, Integer limit) {
        return repo.findAll(PageRequest.of(offset, limit));
    }

    public Posts save(Posts posts) {
        return repo.save(posts);
    }
    
    public Posts findById(Integer id) {
        return repo.getById(id);
    }
    
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
