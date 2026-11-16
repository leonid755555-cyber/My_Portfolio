package postgres.core.dto.queries;

import java.time.LocalDate;

public interface RepairInfoDTO {
    LocalDate getDateOfReceipt();
    LocalDate getCompletionDate();
    Integer gettransportId();
    String getworkType();
    Integer getCostOfRepair();
    Integer getEmplId();
    String getName();
}
