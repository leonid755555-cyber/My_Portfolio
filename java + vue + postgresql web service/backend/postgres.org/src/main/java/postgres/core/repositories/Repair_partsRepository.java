package postgres.core.repositories;

import postgres.core.entities.Repair_parts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repair_partsRepository extends JpaRepository<Repair_parts, Integer> {
}
