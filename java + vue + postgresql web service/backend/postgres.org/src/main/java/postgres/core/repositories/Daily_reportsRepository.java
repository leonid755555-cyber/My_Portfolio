package postgres.core.repositories;

import postgres.core.entities.Daily_reports;
import postgres.core.dto.queries.MileageDTO;
import postgres.core.dto.queries.CargoTransportationDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

import java.time.LocalDate;

@Repository
public interface Daily_reportsRepository extends JpaRepository<Daily_reports, Integer> {


    @Query(value =  "SELECT sum(dr.mileage) AS Mileage" +
          "          FROM Daily_reports AS dr" +
          "                   INNER JOIN Transport AS tr ON dr.transport_id = tr.id" +
          "                   INNER JOIN Types AS t ON t.id = tr.type_id" +
          "          WHERE ((dr.transport_id = CAST(CAST(:auto_Id as text) AS integer) OR :auto_Id IS NULL) AND" +
          "                (t.name = CAST(CAST(:typeName as text) AS varchar) OR :typeName IS NULL)) AND" +
          "                ((CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NOT NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NOT NULL AND dr.report_date BETWEEN CAST(CAST(CAST(:startDate as text)as varchar) AS date) AND CAST(CAST(CAST(:startDate as text)as varchar) AS date)) OR" +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NOT NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NULL AND dr.report_date >= CAST(CAST(CAST(:startDate as text)as varchar) AS date)) OR" +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NOT NULL AND dr.report_date <= CAST(CAST(CAST(:endDate as text)as varchar) AS date)) OR " +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NOT NULL))", nativeQuery = true) 
    List<MileageDTO> getMileageDTO(@Param("auto_Id") Integer autoId,
                                   @Param("typeName") String typeName,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);

    @Query(value =  "SELECT sum(dr.cargo_transportation) AS CargoTransportation" +
          "          FROM Daily_reports AS dr" +
          "                   INNER JOIN Transport AS tr ON dr.transport_id = tr.id" +
          "                   INNER JOIN Types AS t ON t.id = tr.type_id" +
          "          WHERE ((dr.transport_id = CAST(CAST(:auto_Id as text) AS integer) OR :auto_Id  IS NULL) AND" +
          "                (t.name = CAST(CAST(:typeName as text) AS varchar) OR :typeName IS NULL)) AND" +
          "                ((CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NOT NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NOT NULL AND dr.report_date BETWEEN CAST(CAST(CAST(:startDate as text)as varchar) AS date) AND CAST(CAST(CAST(:startDate as text)as varchar) AS date)) OR" +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NOT NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NULL AND dr.report_date >= CAST(CAST(CAST(:startDate as text)as varchar) AS date)) OR" +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NOT NULL AND dr.report_date <= CAST(CAST(CAST(:endDate as text)as varchar) AS date)) OR " +
          "                (CAST(CAST(CAST(:startDate as text)as varchar) AS date) IS NULL AND CAST(CAST(CAST(:endDate as text)as varchar) AS date) IS NULL))", nativeQuery = true) 
    List<CargoTransportationDTO> getCargoTransportationDTO(@Param("auto_Id") Integer autoId,
                                                           @Param("typeName") String typeName,
                                                           @Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);
}