package imhere_backend.API.DTO;

import imhere_backend.dataBase.tables.Tags;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseDto {
    private Long id;
    private String nickname;
    private String description;
    private String link_to_avatar;
    private List<Tags> tags;
}
