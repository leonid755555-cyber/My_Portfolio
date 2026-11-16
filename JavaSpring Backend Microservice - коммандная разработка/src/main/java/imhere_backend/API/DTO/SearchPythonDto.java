package imhere_backend.API.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchPythonDto {
    private String query;
    private Long limit;
    private Long offset;
    private List<Long> tags;
}

