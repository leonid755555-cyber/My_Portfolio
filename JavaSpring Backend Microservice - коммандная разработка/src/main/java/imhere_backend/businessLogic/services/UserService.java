package imhere_backend.businessLogic.services;

import imhere_backend.dataBase.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean isPasswordCorrect(String username, String password) {
        var userFromDB = userRepository.findByEmail(username);

        if(userFromDB.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(password, userFromDB.get().getPassword());
    }

    public Long getUserId(String email) {
        var userFromDB = userRepository.findByEmail(email);

        return userFromDB.get().getUser_id();
    }
}
