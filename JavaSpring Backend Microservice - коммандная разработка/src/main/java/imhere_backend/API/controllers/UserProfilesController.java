package imhere_backend.API.controllers;

import imhere_backend.API.DTO.EditUserProfileRequestDto;
import imhere_backend.API.DTO.UploadImageDto;
import imhere_backend.API.DTO.UserProfileDto;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.businessLogic.services.UserProfilesService;
import imhere_backend.dataBase.tables.UserProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfilesController {

    private final UserProfilesService userProfilesService;

    @GetMapping("/{user_id}")
    @Transactional
    public UserProfileDto getUserProfileById(@PathVariable Long user_id) {
        return userProfilesService.getUserProfileById(user_id);
    }

    @PutMapping("/{user_id}")
    @Transactional
    public UserProfileDto editProfile(@PathVariable Long user_id,
                                      @RequestBody EditUserProfileRequestDto request) {
        UserProfiles userProfile = userProfilesService.editUserProfile(user_id, request);
        return UserProfileDto.builder()
                .nickname(userProfile.getNickname())
                .status(userProfile.getStatus())
                .description(userProfile.getDescription())
                .birthday(userProfile.getBirthday())
                .sex(userProfile.getSex())
                .build();
    }

    @PostMapping("/{user_id}")
    @Transactional
    public UserProfileDto addProfile(@PathVariable Long user_id,
                                      @RequestBody EditUserProfileRequestDto request) {
        UserProfiles userProfile = userProfilesService.addUserProfile(user_id, request);
        return UserProfileDto.builder()
                .nickname(userProfile.getNickname())
                .status(userProfile.getStatus())
                .description(userProfile.getDescription())
                .birthday(userProfile.getBirthday())
                .sex(userProfile.getSex())
                .build();
    }

    @PutMapping("/avatar/{user_id}")
    public UploadImageDto uploadImage(@PathVariable Long user_id,
                                      @RequestParam("image") MultipartFile imageFile) {
        try {
            return UploadImageDto.builder()
                    .url(userProfilesService.uploadImage(user_id, imageFile))
                    .build();
        } catch (IOException e) {
            throw new CustomException("Failed to upload image.");
        }
    }
}
