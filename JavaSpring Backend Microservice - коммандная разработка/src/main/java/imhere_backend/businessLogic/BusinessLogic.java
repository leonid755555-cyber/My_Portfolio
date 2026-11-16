package imhere_backend.businessLogic;

import imhere_backend.businessLogic.authorization.auth.AuthenticationRequest;
import imhere_backend.businessLogic.authorization.auth.AuthenticationResponse;
import imhere_backend.businessLogic.authorization.auth.AuthenticationService;
import imhere_backend.businessLogic.authorization.auth.Output;
import imhere_backend.dataBase.tables.User;
import imhere_backend.businessLogic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BusinessLogic
{
//    @Autowired
//    public BusinessLogic(AuthenticationService authenticationService, UserService userService) {
//        this.authenticationService = authenticationService;
//        this.userService = userService;
//    }
//
//    public String register(User user)
//    {
//        return authenticationService.register(user);
//    }
//
//    public void refreshToken(HttpServletRequest request,
//                             HttpServletResponse response
//    ) throws IOException
//    {
//        authenticationService.refreshToken(request, response);
//    }
//
//    public Output getJwtToken(AuthenticationRequest request)
//    {
//
//        if (!userService.isPasswordCorrect(request.getEmail(), request.getPassword()))
//        {
//            return null;
//        }
//
//        Output output = new Output();
//
//        output.id=userService.getUserId(request.getEmail());
//        output.token = AuthenticationResponse.builder()
//                .accessToken(authenticationService.authenticate(request))
//                .build();
//
//        return output;
//    }
//
//    private final AuthenticationService authenticationService;
//    private final UserService userService;
}
