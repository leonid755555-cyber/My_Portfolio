package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfilePythonDto {
    private Long id;
    private DataPython data;

    private List<Long> tags;
}
