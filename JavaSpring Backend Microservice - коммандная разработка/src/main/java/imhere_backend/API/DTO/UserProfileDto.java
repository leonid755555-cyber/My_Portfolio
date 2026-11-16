package imhere_backend.API.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import imhere_backend.dataBase.tables.Enums.Sex;
import imhere_backend.dataBase.tables.Tags;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Builder
public class UserProfileDto {
    private String nickname;

    private String status;

    private String description;

    private Date birthday;

    private Sex sex;

    @JsonProperty("link_to_avatar")
    private String LinkToAvatar;

    private List<Tags> tags;
}
