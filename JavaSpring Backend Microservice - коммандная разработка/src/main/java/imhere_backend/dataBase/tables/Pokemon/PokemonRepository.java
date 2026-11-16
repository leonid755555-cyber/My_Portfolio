package imhere_backend.dataBase.tables.Pokemon;

import org.springframework.data.jpa.repository.JpaRepository;
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
}