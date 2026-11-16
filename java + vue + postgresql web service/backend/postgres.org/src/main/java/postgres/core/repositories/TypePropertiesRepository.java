package postgres.core.repositories;

import postgres.core.entities.TypeProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePropertiesRepository extends JpaRepository<TypeProperties, Integer> {
}
