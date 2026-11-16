package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String email;
    private Long user_id;
}
