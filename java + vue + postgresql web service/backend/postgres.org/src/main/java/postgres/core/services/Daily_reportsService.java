package postgres.core.services;

import postgres.core.dto.Daily_reportsDTO;
import postgres.core.dto.queries.MileageDTO;
import postgres.core.dto.queries.CargoTransportationDTO;
import postgres.core.entities.Daily_reports;
import postgres.core.mapper.Daily_reportsMapper;
import postgres.core.repositories.Daily_reportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Service
public class Daily_reportsService {
    private final Daily_reportsRepository repo;

    private final Daily_reportsMapper daily_reportsMapper;

    @Autowired
    public Daily_reportsService(Daily_reportsRepository repo, Daily_reportsMapper daily_reportsMapper) {
        this.repo = repo;
        this.daily_reportsMapper = daily_reportsMapper;
    }
    public Page<Daily_reportsDTO> getAll(Integer offset, Integer limit) {
        Page<Daily_reports> Daily_reports = repo.findAll(PageRequest.of(offset, limit));
        return daily_reportsMapper.entitiesToDtos(Daily_reports);
    }


    public List<MileageDTO> getMileageDTO(Integer autoId,
    									  String typeName,
    									  String startDate,
    									  String endDate) {
        return repo.getMileageDTO(autoId,
                                  typeName,
                                  startDate,
                                  endDate);
    }

    public List<CargoTransportationDTO> getCargoTransportationDTO(Integer autoId,
    															  String typeName,
    															  String startDate,
    															  String endDate) {
        return repo.getCargoTransportationDTO(autoId,
                                              typeName,
                                              startDate,
                                              endDate);
    }

    public Daily_reports save(Daily_reportsDTO daily_reportsDTO) {
        return repo.save(daily_reportsMapper.dtoToEntity(daily_reportsDTO));
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
