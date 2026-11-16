package postgres.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import org.hibernate.annotations.Type;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmplDTO implements BaseDTO {
    private Integer id;
    private String fio;
    private Integer postId;
    private Integer headId;
}
