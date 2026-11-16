package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {
}