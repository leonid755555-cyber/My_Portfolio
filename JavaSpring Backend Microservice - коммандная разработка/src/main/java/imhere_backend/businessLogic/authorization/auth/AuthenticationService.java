package imhere_backend.businessLogic.authorization.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.dataBase.repositories.UserRepository;
import imhere_backend.businessLogic.authorization.security.JwtService;
import imhere_backend.businessLogic.authorization.security.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import imhere_backend.dataBase.tables.User;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(User request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        var userFromDB = this.repository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            logger.info("неверный логин или пароль");
            return null;
        }
        var newUser = repository.save(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .id(newUser.getUser_id())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new CustomException("Введен неверный адрес или пароль");
        }
        var user = repository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return null;
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(new UserDetailsImpl(user.get())))
                .id(user.get().getUser_id())
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByEmail(username)
                    .orElseThrow();
            if (jwtService.isTokenExpired(refreshToken, new UserDetailsImpl(user))) {
                var accessToken = jwtService.generateToken(new UserDetailsImpl(user));
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
