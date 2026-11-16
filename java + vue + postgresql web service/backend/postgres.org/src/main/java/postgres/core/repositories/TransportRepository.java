package postgres.core.repositories;

import postgres.core.entities.Transport;
import postgres.core.dto.queries.AutoListDTO;
import postgres.core.dto.queries.sqlDTO;
import postgres.core.dto.queries.AutoPlaceInfoDTO;
import postgres.core.dto.queries.PurchasedDTO;
import postgres.core.dto.queries.DecommissionedDTO;
import postgres.core.dto.queries.PlaceInfoDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
	
@Repository
public interface TransportRepository extends JpaRepository<Transport, Integer> {

    @Query(value = "SELECT" + 
           "               tr.id as TransportId," +
           "               t.name as TypeName," +
           "               p.name as PlaceName," +
           "               tr.dt as DateOfPurchase," +
           "               tr.working_life as WorkingLife" +
           "        FROM Transport AS tr" +
           "                 INNER JOIN Types AS t ON tr.type_id = t.id" +
           "                 INNER JOIN Places AS p ON p.id = tr.place_id", nativeQuery = true)
    List<AutoListDTO> getAutoList();

    @Query(value = "SELECT " +
          "                 tr.id as TransportId," +
          "                 p.name as PlaceName" +
          "         FROM Transport AS tr" +
          "                 INNER JOIN Places AS p ON p.id = tr.place_id" + 
          "         ORDER BY tr.id", nativeQuery = true)
    List<AutoPlaceInfoDTO> getAutoPlaceInfo();

    @Query(value = "SELECT" +
            "               tr.id as TransportId," +
            "               t.name as TypeName," +
            "               p.name as PlaceName," +
            "               tr.dt as DateOfPurchase," +
            "               tr.working_life as WorkingLife" +
          "         FROM Transport AS tr" +
          "             INNER JOIN Types AS t ON tr.type_id = t.id" +
          "             INNER JOIN Places AS p ON p.id = tr.place_id" +
          "         WHERE" +
          "               :startDate IS NOT NULL AND :endDate IS NOT NULL" +
  //        "                (CAST(CAST(:startDate as text) AS localdate) IS NOT NULL AND CAST(CAST(:endDate as text) AS localdate) IS NULL AND tr.dt >= CAST(CAST(:startDate as text) AS localdate)) OR" +
    //      "                (CAST(CAST(:startDate as text) AS localdate) IS NULL AND CAST(CAST(:endDate as text) AS localdate) IS NOT NULL AND tr.dt <= CAST(CAST(:endDate as text) AS localdate)) OR " +
      //    "                (CAST(CAST(:startDate as text) AS localdate) IS NULL AND CAST(CAST(:endDate as text) AS localdate) IS NULL)" +
          "         ORDER BY dateOfPurchase", nativeQuery = true)
    List<DecommissionedDTO> getDecommissioned(@Param("startDate") LocalDate startDate, @Param("endDate")   LocalDate endDate);
    
    @Query(value = "SELECT" +
            "               tr.id as TransportId," +
            "               t.name as TypeName," +
            "               p.name as PlaceName," +
            "               tr.dt as DateOfPurchase," +
            "               tr.working_life as WorkingLife" +
            "         FROM Transport AS tr" +
            "             INNER JOIN Types AS t ON tr.type_id = t.id" +
            "             INNER JOIN Places AS p ON p.id = tr.place_id" +
            "         WHERE" +
            "               :startDate IS NOT NULL AND :endDate IS NOT NULL" +
  //          "                (CAST(CAST(:startDate as text) AS localdate) IS NOT NULL AND CAST(CAST(:endDate as text) AS localdate) IS NULL AND tr.working_life >= CAST(CAST(:startDate as text) AS localdate)) OR" +
    //        "                (CAST(CAST(:startDate as text) AS localdate) IS NULL AND CAST(CAST(:endDate as text) AS localdate) IS NOT NULL AND tr.working_life <= CAST(CAST(:endDate as text) AS localdate)) OR " +
      //      "                (CAST(CAST(:startDate as text) AS localdate) IS NULL AND CAST(CAST(:endDate as text) AS localdate) IS NULL)" +
            "         ORDER BY dateOfPurchase", nativeQuery = true)
      List<PurchasedDTO> getPurchased(@Param("startDate") LocalDate startDate, @Param("endDate")   LocalDate endDate);
    
    @Query(value =  "SELECT distinct p.name as PlaceName," +
            "                 t.name as TypeName" +  
            "          FROM Transport AS tr" +
            "                   INNER JOIN Types AS t ON tr.type_id = t.id" + 
            "                   INNER JOIN Places AS p ON p.id = tr.place_id" +
            "          WHERE t.name = CAST(CAST(:tName as text) AS varchar) OR :tName = NULL" +
            "          ORDER BY p.name", nativeQuery = true)
  List<PlaceInfoDTO> getPlaceInfo(@Param("tName") String tName);
  
}