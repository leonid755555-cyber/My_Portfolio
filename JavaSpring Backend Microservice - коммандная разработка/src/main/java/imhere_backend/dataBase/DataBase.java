package imhere_backend.dataBase;

import imhere_backend.API.DTO.*;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.businessLogic.services.PythonApiService;
import imhere_backend.businessLogic.services.UserProfilesService;
import imhere_backend.dataBase.repositories.TagsUsersRepository;
import imhere_backend.dataBase.repositories.UserProfilesRepository;
import imhere_backend.dataBase.tables.TagsUsers;
import imhere_backend.dataBase.tables.UserProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DataBase {

    private final PythonApiService pythonApiService;
    private final UserProfilesService userProfilesService;
    private final TagsUsersRepository tagsUsersRepository;
    private final UserProfilesRepository userProfilesRepository;
/*
    public List<SearchResponseDto> searchUsersByDes(SearchDto searchDto) {
        ResponseEntity<SearchPythonResponseDto> responseUsers = pythonApiService.searchUsers(SearchPythonDto.builder()
                .query(searchDto.getDescription())
                .limit((long)50)
                .tags(null)
                .build());
        List<UserProfiles> userProfiles = responseUsers.getBody().getId().stream().map(profileId -> userProfilesRepository.findByIdUser(profileId).orElseThrow(() -> new CustomException("Такого пользователя не существует")))
                .toList();
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
    public List<SearchResponseDto> searchUsersByTags(SearchDto searchDto) {

        ResponseEntity<SearchPythonResponseDto> responseUsers = pythonApiService.searchUsers(SearchPythonDto.builder()
                .query("")
                .limit((long)50)
                .tags(searchDto.getTags())
                .build());
        List<UserProfiles> userProfiles = responseUsers.getBody().getId().stream().map(profileId -> userProfilesRepository.findByIdUser(profileId).orElseThrow(() -> new CustomException("Такого пользователя не существует")))
                .toList();
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
    }*/
    public List<SearchResponseDto> searchUsersByParams(SearchDto searchDto) {

        ResponseEntity<SearchPythonResponseDto> responseUsers = pythonApiService.searchUsers(SearchPythonDto.builder()
                .query(searchDto.getDescription())
                .limit((long)50)
                .offset((long)0)
                .tags(searchDto.getTags())
                .build());
        Stream<UserProfiles> userProfiles = responseUsers.getBody().getId().stream()
                .map(profileId -> userProfilesRepository.findByIdUser(profileId).orElse(null));
        return userProfiles
                .filter(profile -> profile != null)
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