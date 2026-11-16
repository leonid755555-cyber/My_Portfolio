package postgres.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportDTO implements BaseDTO {
    private Integer id;
    private Integer typeId;
    private Integer placeId;
    private String typeName;
    private String placeName;
    private LocalDate dt1;
    private LocalDate working_life;
}
