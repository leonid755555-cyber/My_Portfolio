package postgres.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypePropertiesDTO implements BaseDTO {
    private int id;
    private int typeId;
    private String  name;
}
