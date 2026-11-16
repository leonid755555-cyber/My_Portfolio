package imhere_backend.businessLogic.services;
import imhere_backend.API.DTO.ProfilePythonDto;
import imhere_backend.API.DTO.ProfilePythonResponseDto;
import imhere_backend.API.DTO.SearchPythonDto;
import imhere_backend.API.DTO.SearchPythonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class PythonApiService {
    private final RestTemplate restTemplate;
    private final String url = "https://imhere.space:6363/api";

    public ResponseEntity<ProfilePythonResponseDto> addProfile(ProfilePythonDto message) {
        String path = url + "/operation/addprofile";
        return restTemplate.postForEntity(path, message, ProfilePythonResponseDto.class);
    }

    public ResponseEntity<ProfilePythonResponseDto> editProfile(ProfilePythonDto message) {
        String path = url + "/operation/editprofile";
        return restTemplate.postForEntity(path, message, ProfilePythonResponseDto.class);
    }

    public ResponseEntity<SearchPythonResponseDto> searchUsers(SearchPythonDto message) {
        String path = url + "/search/text";
        return restTemplate.postForEntity(path, message, SearchPythonResponseDto.class);
    }
}
