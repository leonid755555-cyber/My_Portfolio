package imhere_backend.businessLogic.services;

import imhere_backend.API.DTO.DataPython;
import imhere_backend.API.DTO.ProfilePythonDto;
import imhere_backend.API.DTO.TagsIdDto;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.dataBase.repositories.TagsRepository;
import imhere_backend.dataBase.repositories.TagsUsersRepository;
import imhere_backend.dataBase.repositories.UserProfilesRepository;
import imhere_backend.dataBase.tables.Tags;
import imhere_backend.dataBase.tables.TagsUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;
    private final TagsUsersRepository tagsUsersRepository;
    private final UserProfilesRepository userProfilesRepository;
    private final PythonApiService pythonApiService;

    public List<Tags> findAll() {
        return tagsRepository.findAll();
    }

    @Transactional
    public void editProfileTags(Long user_id, TagsIdDto tagsDto) {
        var userProfile = userProfilesRepository.findByIdUser(user_id)
                .orElseThrow(() -> new CustomException("Такой пользователь не найден"));
        var tagsFromDB = tagsUsersRepository.findAllByUserProfileId(userProfile.getId()).stream()
                .map(TagsUsers::getTag)
                .map(Tags::getId)
                .toList();
        var existingTagsSet = new HashSet<>(tagsFromDB);
        var newTags = tagsDto.getTags().stream()
                .filter(tag -> !existingTagsSet.contains(tag))
                .toList();
        newTags.forEach(tag -> tagsUsersRepository.save(TagsUsers.builder()
                .tag(tagsRepository.findById(tag).get())
                .userProfile(userProfile)
                .build()));

        pythonApiService.editProfile(ProfilePythonDto.builder()
                .id(userProfile.getId())
                .data(DataPython.builder()
                        .status(userProfile.getStatus())
                        .name(userProfile.getNickname())
                        .description(userProfile.getDescription())
                        .build())
                .tags(newTags)
                .build());
    }

    @Transactional
    public List<Tags> getProfileTags(Long user_id) {
        var userProfile = userProfilesRepository.findByIdUser(user_id)
                .orElseThrow(() -> new CustomException("Такой пользователь не найден"));
        return tagsUsersRepository.findAllByUserProfileId(userProfile.getId()).stream()
                .map(TagsUsers::getTag)
                .toList();
    }

    @Transactional
    public Tags save(Tags tags) {
        return tagsRepository.save(tags);
    }

    @Transactional
    public void delete(Long id) {
        tagsRepository.deleteById(id);
    }
}

