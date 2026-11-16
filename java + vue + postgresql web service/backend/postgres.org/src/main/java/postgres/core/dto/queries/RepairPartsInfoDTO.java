package postgres.core.dto.queries;

public interface RepairPartsInfoDTO {
    String getPartName();
    Integer getTransportId();
    String getTypeName();
    Integer getCountOfParts();
}
