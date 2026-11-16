package postgres.core.repositories;

import postgres.core.entities.Transport_empl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Transport_emplRepository extends JpaRepository<Transport_empl, Integer> {
}
