package imhere_backend.API.DTO;

import imhere_backend.dataBase.tables.Enums.Sex;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class EditUserProfileRequestDto {
    private String nickname;

    private String status;

    private String description;

    private Date birthday;

    private Sex sex;
}
