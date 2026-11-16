package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPython {
    private String name;

    private String status;

    private String description;

    private String image;
}
