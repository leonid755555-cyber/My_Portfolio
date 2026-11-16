package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.ResoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ResoursesService {
    @Autowired
    private ResoursesRepository resoursesRepository;


}
