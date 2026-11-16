package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.CommentsProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CommentsProjectsService {
    @Autowired
    private CommentsProjectsRepository commentsProjectsRepository;


}
