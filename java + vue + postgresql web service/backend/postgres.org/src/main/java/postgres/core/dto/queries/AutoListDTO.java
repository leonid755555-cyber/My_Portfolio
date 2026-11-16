package postgres.core.dto.queries;

import java.time.LocalDate;

public interface AutoListDTO {
    Integer getTransportId();
    String getTypeName();
    String getPlaceName();
    LocalDate getDateOfPurchase();
    LocalDate getWorkingLife();
}
