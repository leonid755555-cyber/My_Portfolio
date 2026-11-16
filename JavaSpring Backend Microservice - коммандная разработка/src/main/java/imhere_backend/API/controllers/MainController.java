package imhere_backend.API.controllers;

import imhere_backend.dataBase.tables.Pokemon.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "${http://localhost:5173}")
@RestController
public class MainController {

    public MainController(PokemonService pokimonService) {
        this.pokimonService=pokimonService;
    }

    @GetMapping("/api/auth/count/pokimon")
    public ResponseEntity<String> exampleMethod()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Integer dataFromDatabase = pokimonService.getCountPokimon();
        return new ResponseEntity<>("{\"message\": \""+dataFromDatabase+"\"}", headers, HttpStatus.OK);
    }

    @PostMapping("/api/add/pokimon")
    public ResponseEntity<String>  addPokimon()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        pokimonService.addPokimon();
        logger.info("new Pokimon found! count: "+pokimonService.getCountPokimon());
        return new ResponseEntity<>("{\"message\": \""+"successful found!"+"\"}", headers, HttpStatus.OK);
    }
    private final PokemonService pokimonService;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

}
