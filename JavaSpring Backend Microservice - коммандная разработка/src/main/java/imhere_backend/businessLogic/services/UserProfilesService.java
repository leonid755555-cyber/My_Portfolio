package imhere_backend.businessLogic.services;

import imhere_backend.API.DTO.*;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.dataBase.repositories.TagsUsersRepository;
import imhere_backend.dataBase.repositories.UserProfilesRepository;
import imhere_backend.dataBase.repositories.UserRepository;
import imhere_backend.dataBase.tables.Tags;
import imhere_backend.dataBase.tables.TagsUsers;
import imhere_backend.dataBase.tables.UserProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfilesService {
    private final UserProfilesRepository userProfilesRepository;
    private final TagsUsersRepository tagsUsersRepository;
    private final UserRepository userRepository;
    private final PythonApiService pythonApiService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-path}")
    private String uploadPathToUrl;

    @Transactional
    public UserProfileDto getUserProfileById(Long user_id) {
        var userProfile = userProfilesRepository.findByIdUser(user_id)
                .orElseThrow(() -> new CustomException("Такого пользователя не существует"));
        var tags = tagsUsersRepository.findAllByUserProfileId(userProfile.getId());
        return UserProfileDto.builder()
                .nickname(userProfile.getNickname())
                .sex(userProfile.getSex())
                .birthday(userProfile.getBirthday())
                .status(userProfile.getStatus())
                .description(userProfile.getDescription())
                .LinkToAvatar(userProfile.getLinkToAvatar())
                .tags(tags.stream()
                        .map(TagsUsers::getTag)
                        .toList())
                .build();
    }

    @Transactional
    public UserProfiles editUserProfile(Long user_id, EditUserProfileRequestDto newUserProfile) {
        var userProfile = userProfilesRepository.findByIdUser(user_id).orElse(new UserProfiles());
        var user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException("Такого пользователя не существует"));
        var tags = tagsUsersRepository.findAllByUserProfileId(userProfile.getId());

        userProfile.setUser(user);
        userProfile.setNickname(newUserProfile.getNickname());
        userProfile.setStatus(newUserProfile.getStatus());
        userProfile.setDescription(newUserProfile.getDescription());
        userProfile.setSex(newUserProfile.getSex());
        userProfile.setBirthday(newUserProfile.getBirthday());

        var response = pythonApiService.editProfile(ProfilePythonDto.builder()
                .id(userProfile.getId())
                .data(DataPython.builder()
                        .name(newUserProfile.getNickname())
                        .status(newUserProfile.getStatus())
                        .description(newUserProfile.getDescription())
                        .build())
                .tags(tags.stream()
                        .map(TagsUsers::getTag)
                        .map(Tags::getId)
                        .toList())
                .build());

        if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return userProfilesRepository.save(userProfile);
        } else {
            throw new CustomException(response.getBody().getAnswer());
        }
    }

    @Transactional
    public UserProfiles addUserProfile(Long user_id, EditUserProfileRequestDto newUserProfile) {
        var user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException("Такого пользователя не существует"));

        var userProfile = UserProfiles.builder()
                .user(user)
                .nickname(newUserProfile.getNickname())
                .status(newUserProfile.getStatus())
                .description(newUserProfile.getDescription())
                .sex(newUserProfile.getSex())
                .birthday(newUserProfile.getBirthday())
                .build();

        var savedProfile = userProfilesRepository.save(userProfile);

        var response = pythonApiService.addProfile(ProfilePythonDto.builder()
                .id(savedProfile.getId())
                .data(DataPython.builder()
                        .name(newUserProfile.getNickname())
                        .status(newUserProfile.getStatus())
                        .description(newUserProfile.getDescription())
                        .build())
                .tags(Collections.emptyList())
                .build());

        if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return userProfilesRepository.save(userProfile);
        } else {
            // TODO удалить из базы
            throw new CustomException(response.getBody().getAnswer());
        }
    }

    @Transactional
    public String uploadImage(Long user_id, MultipartFile imageFile) throws IOException {
        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);

        Files.write(filepath, imageFile.getBytes());

        String fileUrl = uploadPathToUrl + filename;

        var userProfile = userProfilesRepository.findByIdUser(user_id).orElse(new UserProfiles());
        if (userProfile.getLinkToAvatar() != null) deleteLastAvatar(userProfile.getLinkToAvatar());
        userProfile.setLinkToAvatar(fileUrl);

        userProfilesRepository.save(userProfile);

        return fileUrl;
    }

    private void deleteLastAvatar(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir, fileName);
        File file = filePath.toFile();
        if (!file.exists() || !file.delete()) {
            throw new CustomException("Ошибка при удалении файла с прошлой аватаркой");
        }
    }

    public List<SearchResponseDto> getRecommendUsers() {
        var userProfiles = userProfilesRepository.findAll();
        return userProfiles.stream()
                .map(profile -> SearchResponseDto.builder()
                        .id(profile.getId())
                        .description(profile.getDescription())
                        .link_to_avatar(profile.getLinkToAvatar())
                        .nickname(profile.getNickname())
                        .tags(
                                tagsUsersRepository.findAllByUserProfileId(profile.getId()).stream()
                                        .map(TagsUsers::getTag)
                                        .toList()
                        )
                        .build())
                .toList();
    }
}
