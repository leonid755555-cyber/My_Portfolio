package postgres.core.repositories;

import postgres.core.entities.Type_properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Type_propertiesRepository extends JpaRepository<Type_properties, Integer> {
}
