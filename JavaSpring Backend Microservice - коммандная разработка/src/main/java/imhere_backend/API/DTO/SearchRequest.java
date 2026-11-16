package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchRequest {
    private String description;
    private List<Long> tags;
}
