package postgres.core.repositories;

import postgres.core.entities.Type_value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Type_valueRepository extends JpaRepository<Type_value, Integer> {
}
