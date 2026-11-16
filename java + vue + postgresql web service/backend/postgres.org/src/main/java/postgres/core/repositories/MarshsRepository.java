package postgres.core.repositories;

import postgres.core.entities.Marshs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarshsRepository extends JpaRepository<Marshs, Integer> {
}
