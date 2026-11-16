package postgres.core.services;

import postgres.core.dto.Repair_reportsDTO;
import postgres.core.dto.queries.RepairCountAndCostDTO;
import postgres.core.dto.queries.RepairPartsInfoDTO;
import postgres.core.dto.queries.RepairInfoDTO;
import postgres.core.entities.Repair_reports;
import postgres.core.mapper.Repair_reportsMapper;
import postgres.core.mapper.TypePropertiesMapper;
import postgres.core.repositories.Repair_reportsRepository;
import postgres.core.repositories.TypePropertiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class Repair_reportsService {
    private final Repair_reportsRepository repo;

    private final Repair_reportsMapper repair_reportsMapper;

    public Page<Repair_reportsDTO> getAll(Integer offset, Integer limit) {
        Page<Repair_reports> repair_reports = repo.findAll(PageRequest.of(offset, limit));
        return repair_reportsMapper.entitiesToDtos(repair_reports);
    }


    public List<RepairCountAndCostDTO> getRepairCountAndCost(Integer autoId,
                                                             Integer typeName,
                                                             LocalDate startDate,
                                                             LocalDate endDate) {
        return repo.getRepairCountAndCost(autoId,
                                          typeName,
                                          startDate,
                                          endDate);
    }

    public List<RepairPartsInfoDTO> getRepairPartsInfo(Integer autoId,
                                                       Integer tName,
                                                       Integer partsList,
                                                       LocalDate startDate,
                                                       LocalDate endDate) {
        return repo.getRepairPartsInfo(autoId,
                                       tName,
                                       partsList,
                                       startDate,
                                       endDate);
    }

    public List<RepairInfoDTO> getRepairInfo(Integer autoId,
                                             Integer tName,
                                             LocalDate startDate,
                                             LocalDate endDate) {
        return repo.getRepairInfo(autoId,
                                  tName,
                                  startDate,
                                  endDate);
    }

    public Repair_reports save(Repair_reportsDTO repair_reportsDTO) {
        return repo.save(repair_reportsMapper.dtoToEntity(repair_reportsDTO));
    }
    
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
