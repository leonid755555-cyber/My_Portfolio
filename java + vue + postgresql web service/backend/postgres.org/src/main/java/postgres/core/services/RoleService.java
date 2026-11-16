package postgres.core.services;

import postgres.core.entities.Role;
import postgres.core.repositories.RoleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService{
    private final RoleRepository repo;

    public List<Role> getAll() {
        return repo.findAll();
    }
    
    public Role getType(long id) {
    	return repo.findById(id).get();
    }
    
    public Role save(Role role) {
        return repo.save(role);
    }
    public void delete(long id) {
        repo.deleteById(id);
    }

}

