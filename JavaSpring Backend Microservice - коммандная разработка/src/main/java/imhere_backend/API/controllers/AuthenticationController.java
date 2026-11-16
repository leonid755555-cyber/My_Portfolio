package imhere_backend.API.controllers;

import imhere_backend.businessLogic.authorization.auth.AuthenticationRequest;
import imhere_backend.businessLogic.authorization.auth.AuthenticationResponse;
import imhere_backend.businessLogic.authorization.auth.AuthenticationService;
import imhere_backend.businessLogic.exceptions.CustomException;
import imhere_backend.dataBase.tables.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid User user
    ) {
        var authResponse = authenticationService.register(user);

        if (authResponse.getAccessToken() == null) {
            throw new CustomException("Invalid e-mail or password");
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        var authResponse = authenticationService.authenticate(request);

        if (authResponse.getAccessToken() == null) {
            throw new CustomException("Invalid e-mail or password");
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
