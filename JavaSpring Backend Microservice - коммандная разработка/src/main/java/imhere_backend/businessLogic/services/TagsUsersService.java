package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.TagsUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TagsUsersService {
    @Autowired
    private TagsUsersRepository tagsUsersRepository;

}
