package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.TagCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagCategoriesRepository extends JpaRepository<TagCategories, Long> {
}