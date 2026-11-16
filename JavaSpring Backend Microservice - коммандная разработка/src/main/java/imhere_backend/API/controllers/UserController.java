package imhere_backend.API.controllers;

import imhere_backend.API.DTO.UserDto;
import imhere_backend.dataBase.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @Transactional
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .user_id(user.getUser_id())
                        .email(user.getEmail())
                        .build())
                .toList();
    }
}
