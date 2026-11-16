package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.TagsUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsUsersRepository extends JpaRepository<TagsUsers, Long> {
    @Query(value = "SELECT * FROM tags_users WHERE id_user_profile =?1", nativeQuery = true)
    public List<TagsUsers> findAllByUserProfileId(Long userProfileId);
}