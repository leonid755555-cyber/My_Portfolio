package imhere_backend.API.controllers;

import imhere_backend.API.DTO.SearchDto;
import imhere_backend.API.DTO.SearchResponseDto;
import imhere_backend.API.DTO.TagsIdDto;
import imhere_backend.businessLogic.services.UserProfilesService;
import imhere_backend.dataBase.DataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final UserProfilesService userProfilesService;
    private final DataBase dataBase;

    @GetMapping("/recommend")
    public List<SearchResponseDto> getRecommendUsers() {
        return userProfilesService.getRecommendUsers();
    }

    @PostMapping
    public List<SearchResponseDto> getSearchUsers(@RequestBody SearchDto searchDto) {
        if (searchDto.getDescription().isEmpty()) {
            return userProfilesService.getRecommendUsers();
        }
        return dataBase.searchUsersByParams(searchDto);
    }
}
