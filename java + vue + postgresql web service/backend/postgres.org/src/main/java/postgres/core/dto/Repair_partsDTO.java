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
public class Repair_partsDTO implements BaseDTO {
    private Integer id;
    private Integer rrId;
    private Integer partId;
    private Integer count;
}
