package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.Resourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResoursesRepository extends JpaRepository<Resourses, Long> {
}