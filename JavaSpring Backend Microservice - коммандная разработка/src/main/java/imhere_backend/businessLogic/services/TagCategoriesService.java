package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.TagCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TagCategoriesService {
    @Autowired
    private TagCategoriesRepository tagCategoriesRepository;


}
