package imhere_backend.dataBase.repositories;

import imhere_backend.dataBase.tables.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long> {
    @Query(value = "SELECT * FROM user_profiles WHERE id_user =?1", nativeQuery = true)
    public Optional<UserProfiles> findByIdUser(long id_user);
}