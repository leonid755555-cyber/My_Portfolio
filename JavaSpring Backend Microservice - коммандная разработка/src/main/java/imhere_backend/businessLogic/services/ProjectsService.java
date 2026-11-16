package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProjectsService {
    @Autowired
    private ProjectsRepository projectsRepository;


}
