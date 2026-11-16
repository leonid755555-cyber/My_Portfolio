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
public class Repair_reportsDTO implements BaseDTO {
    private Integer id;
    private LocalDate date_of_receipt;
    private LocalDate completion_date;
    private Integer transportId;
    private String work_type;
    private Integer cost;
    private Integer emplId;
}
