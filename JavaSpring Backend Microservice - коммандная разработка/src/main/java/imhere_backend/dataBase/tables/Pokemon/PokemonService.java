package imhere_backend.dataBase.tables.Pokemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PokemonService {
    @Autowired
    private PokemonRepository pokimonRepository;

    public Integer getCountPokimon() {
        List<Pokemon> courses = pokimonRepository.findAll();
        return courses.get(0).getCount();
    }
    public void addPokimon()
    {
        List<Pokemon> courses = pokimonRepository.findAll();
        Pokemon pokimon = courses.get(0);
        pokimon.setCount(pokimon.getCount()+1);
        pokimonRepository.save(pokimon);
    }
    public Pokemon addNewKindPokimon()
    {
        Pokemon pokimon = new Pokemon();
        pokimon.setCount(0);
        return pokimonRepository.save(pokimon);
    }
}
