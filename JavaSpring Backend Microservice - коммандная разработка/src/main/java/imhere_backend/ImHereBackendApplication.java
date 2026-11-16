package imhere_backend;

import imhere_backend.dataBase.tables.Pokemon.PokemonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ImHereBackendApplication {

    public static void main(String[] args)
    {
        ApplicationContext context =SpringApplication.run(ImHereBackendApplication.class, args);

        PokemonService pokimonService = context.getBean(PokemonService.class);
        pokimonService.addNewKindPokimon();
        System.out.println("hi! my name is Lisby. I found pokimons: "+pokimonService.getCountPokimon());

    }

}
