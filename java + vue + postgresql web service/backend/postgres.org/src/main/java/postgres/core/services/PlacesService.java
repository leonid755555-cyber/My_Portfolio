package postgres.core.services;

import postgres.core.dto.queries.PlaceInfoDTO;
import postgres.core.entities.Places;
import postgres.core.entities.Posts;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.PlacesRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlacesService {
    private final PlacesRepository repo;


    public Page<Places> getAll(Integer offset, Integer limit) {
        return repo.findAll(PageRequest.of(offset, limit));
    }


    public Places findById(Integer id) {
        return repo.getById(id);
    }
    
    public Places save(Places places) {
        return repo.save(places);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
