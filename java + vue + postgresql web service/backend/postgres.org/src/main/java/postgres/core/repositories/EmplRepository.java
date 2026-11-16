package postgres.core.repositories;

import postgres.core.entities.Empl;
import postgres.core.dto.queries.DriverListDTO;
import postgres.core.dto.queries.PostInfoDTO;
import postgres.core.dto.queries.SubordinatesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface EmplRepository extends JpaRepository<Empl, Integer> {


        @Query(value =  "SELECT e.id AS emplid," +
              "                 e.fio AS Name," +
              "                 te.transport_id AS transportid," +
              "                 t.name AS typeName" +
              "          FROM Empl AS e" +
              "                 INNER JOIN Transport_empl AS te ON e.id = te.empl_id AND e.post_id = 1" +
              "                 INNER JOIN Transport AS tr ON tr.id = te.transport_id" +
              "                 INNER JOIN Types AS t ON t.id = tr.type_id" +
              "          WHERE te.transport_id = CAST(CAST(:auto as text) AS integer) OR :auto IS NULL" +
              "          order by te.transport_id", nativeQuery = true)
    List<DriverListDTO> getDriverList(@Param("auto") Integer auto);

        @Query(value = "SELECT e.fio AS emplname," +
              "                p.name AS postname" +
              "         FROM Empl AS e" +
              "             INNER JOIN Posts AS p ON p.id = e.post_id" +
              "         ORDER BY name", nativeQuery = true)
        List<PostInfoDTO> getPostInfo();

        @Query(value =  "SELECT" +
              "                 e.id AS emplid," +
              "                 e.fio AS emplname," +
              "                 p.name AS postname" +
              "          FROM Empl AS e" +
              "                 INNER JOIN Posts AS p ON e.post_id = p.id" +
              "          WHERE e.head_Id = CAST(CAST(:person_id as text) AS integer) OR :person_id IS NULL" +
              "          ORDER BY p.name", nativeQuery = true)
    List<SubordinatesDTO> getSubordinates(@Param("person_id") Integer person_Id);
}