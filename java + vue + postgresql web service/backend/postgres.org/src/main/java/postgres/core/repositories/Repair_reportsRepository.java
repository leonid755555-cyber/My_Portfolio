package postgres.core.repositories;

import postgres.core.entities.Repair_reports;
import postgres.core.dto.queries.RepairCountAndCostDTO;
import postgres.core.dto.queries.RepairPartsInfoDTO;
import postgres.core.dto.queries.RepairInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.time.LocalDate;

@Repository
public interface Repair_reportsRepository extends JpaRepository<Repair_reports, Integer> {

        @Query(value = " SELECT " +
              "              count(rr.id) as CountOfRepair," +
              "              sum(rr.cost) as CostOfRepair" +
              "          FROM Types AS t " +
              "                   INNER JOIN Transport AS tr ON t.id = tr.type.id" +
              "                   INNER JOIN Repair_reports AS rr ON rr.transport.id = tr.id" +
              "          WHERE (CAST(CAST(:auto_Id as text) AS integer) IS NOT NULL AND rr.transport.id = CAST(CAST(:auto_Id as text) AS integer) OR CAST(CAST(:auto_Id as text) AS integer) IS NULL) AND" +
              "                (CAST(CAST(:typeName as text) AS integer) IS NOT NULL AND t.name = CAST(CAST(:typeName as text) AS integer) OR CAST(CAST(:typeName as text) AS integer) IS NULL) AND" +
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.date_of_receipt BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.date_of_receipt >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.date_of_receipt <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL)) OR" +      
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.completion_date BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.completion_date >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.completion_date <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL))")
    List<RepairCountAndCostDTO> getRepairCountAndCost(@Param("auto_Id") Integer autoId,
                                                      @Param("typeName") Integer typeName,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

        @Query(value =  "SELECT" +
              "              p.name as PartName," +
              "              tr.id as Transportid," +
              "              t.name as TypeName," +
              "              rp.count as CountOfParts" +
              "          FROM Types AS t1" +
              "                   INNER JOIN Transport AS tr ON t1.id = tr.type.id" +
              "                   INNER JOIN Repair_reports AS rr ON rr.transport.id = tr.id" +
              "                   INNER JOIN Repair_parts AS rp ON rr.id = rp.repair_reports.id" +
              "                   INNER JOIN Parts AS p ON p.id = rp.part.id" +
              "                   INNER JOIN Types AS t ON t.id = tr.type.id" +
              "          WHERE (CAST(CAST(:auto_Id as text) AS integer) IS NOT NULL AND rr.transport.id = CAST(CAST(:auto_Id as text) AS integer) OR CAST(CAST(:auto_Id as text) AS integer) IS NULL) AND" +
              "                (CAST(CAST(:tName as text) AS integer) IS NOT NULL AND t1.name = CAST(CAST(:tName as text) AS integer) OR CAST(CAST(:tName as text) AS integer) IS NULL) AND" +
              "                (CAST(CAST(:partsList as text) AS integer) IS NOT NULL AND p.name = CAST(CAST(:partsList as text) AS integer) OR CAST(CAST(:partsList as text) AS integer) IS NULL) AND" +
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.date_of_receipt BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.date_of_receipt >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.date_of_receipt <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL)) OR" +      
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.completion_date BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.completion_date >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.completion_date <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL))" +    
              "          ORDER BY p.name")
    List<RepairPartsInfoDTO> getRepairPartsInfo(@Param("auto_Id") Integer autoId,
                                                @Param("tName") Integer tName,
                                                @Param("partsList") Integer partsList,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

        @Query(value =  "SELECT" +
              "                 rr.date_of_receipt as date_of_receipt," +
              "                 rr.completion_date as completion_date," +
              "                 rr.transport.id as transportid," +
              "                 rr.work_type as work_type," +
              "                 rr.cost as cost," +
              "                 rr.empl.id as emplid," +
              "                 e.fio as name" +
              "          FROM Repair_reports AS rr" +
              "              INNER JOIN Empl AS e ON e.id = rr.empl.id" +
              "          WHERE (CAST(CAST(:person_Id as text) AS integer) IS NOT NULL AND rr.empl.id = CAST(CAST(:person_Id as text) AS integer) OR CAST(CAST(:person_Id as text) AS integer) IS NULL) AND" +
              "                (CAST(CAST(:t_Id as text) AS integer) IS NOT NULL AND rr.transport.id = CAST(CAST(:t_Id as text) AS integer) OR CAST(CAST(:t_Id as text) AS integer) IS NULL) AND" +
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.date_of_receipt BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.date_of_receipt >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.date_of_receipt <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL)) OR" +      
              "               ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND rr.completion_date BETWEEN :startDate AND :startDate) OR" +
              "                (:startDate IS NOT NULL AND :endDate IS NULL AND rr.completion_date >= :startDate) OR" +
              "                (:startDate IS NULL AND :endDate IS NOT NULL AND rr.completion_date <= :endDate) OR " +
              "                (:startDate IS NULL AND :endDate IS NULL))" +
              "          ORDER BY date_of_receipt")
    List<RepairInfoDTO> getRepairInfo(@Param("person_Id") Integer personId,
                                      @Param("t_Id") Integer tId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}