package postgres.core.repositories;

import postgres.core.dto.queries.ScheduleDTO;
import postgres.core.entities.Schedules;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Integer> {
    @Query(value = "SELECT sh.transport_id as TransportId," +
           "                m.name as MarshrutName" +
           "        FROM Schedules AS sh" +
           "                INNER JOIN Marshs AS m ON m.id = sh.marshrut_id", nativeQuery = true)
    List<ScheduleDTO> getSchedule();

}
