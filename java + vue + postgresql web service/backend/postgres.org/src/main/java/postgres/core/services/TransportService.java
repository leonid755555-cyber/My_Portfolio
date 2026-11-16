package postgres.core.services;

import postgres.core.dto.TransportDTO;
import postgres.core.dto.queries.AutoListDTO;
import postgres.core.dto.queries.AutoPlaceInfoDTO;
import postgres.core.dto.queries.PurchasedDTO;
import postgres.core.dto.queries.sqlDTO;
import postgres.core.dto.queries.DecommissionedDTO;
import postgres.core.dto.queries.PlaceInfoDTO;
import postgres.core.entities.Transport;
import postgres.core.entities.Types;
import postgres.core.mapper.TransportMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.TransportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;  

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class TransportService {

    @Autowired
    private final TransportRepository repo;
    
    @PersistenceContext
    private EntityManager entityManager;

    private final TransportMapper transportMapper;


    public Page<TransportDTO> getAll(Integer offset, Integer limit) {
        Page<Transport> transport = repo.findAll(PageRequest.of(offset, limit));
        return transportMapper.entitiesToDtos(transport);
    }


    public List<AutoListDTO> getAutoList() {
        return repo.getAutoList();
    }


    public List<AutoPlaceInfoDTO> getAutoPlaceInfo() {
        return repo.getAutoPlaceInfo();
    }
    @Transactional
    public int getSQL(String text) {
        return entityManager.createNativeQuery(text).executeUpdate();
    }

    public List<DecommissionedDTO> getDecommissioned(LocalDate startDate, LocalDate endDate) {
        return repo.getDecommissioned(startDate,
                                      endDate);
    }
    
    public List<PurchasedDTO> getPurchased(LocalDate startDate, LocalDate endDate) {
    	return repo.getPurchased(startDate,
    							 endDate);
    }

    public List<PlaceInfoDTO> getPlaceInfo(String tName) {
        return repo.getPlaceInfo(tName);
    }

    public Transport findById(Integer id) {
        return repo.getById(id);
    }
    
    public Transport save(TransportDTO transportDTO) {
        return repo.save(transportMapper.dtoToEntity(transportDTO));
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
