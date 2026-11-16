package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.CommentsProjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsProjectsRepository extends JpaRepository<CommentsProjects, Long> {
}