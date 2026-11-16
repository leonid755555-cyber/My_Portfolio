package postgres.core.repositories;

import postgres.core.entities.Places;
import postgres.core.dto.queries.PlaceInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface PlacesRepository extends JpaRepository<Places, Integer> {

}