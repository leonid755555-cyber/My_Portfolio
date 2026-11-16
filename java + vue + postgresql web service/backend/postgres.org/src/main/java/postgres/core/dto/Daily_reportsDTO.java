package postgres.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Daily_reportsDTO implements BaseDTO {
    private Integer id;
    private Integer transportId;
    private LocalDate report_date;
    private Integer tickets_count;
    private Integer mileage;
    private Integer cargo_transportation;
}
