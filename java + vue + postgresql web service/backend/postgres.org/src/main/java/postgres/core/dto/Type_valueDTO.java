package postgres.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Type_valueDTO implements BaseDTO {
    private Integer id;
    private Integer transportId;
    private Integer propId;
    private Integer value;
}
