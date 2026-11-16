package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilePythonResponseDto {
    Long id;
    String answer;
}
