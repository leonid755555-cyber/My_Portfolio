package postgres.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "daily_reports")
@NoArgsConstructor
@AllArgsConstructor
public class Daily_reports implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportId", nullable = false)
    private Transport transport;

    @Column(name = "report_date", nullable = false)
    private LocalDate report_date;

    @Column(name = "tickets_count", nullable = false)
    private Integer tickets_count;

    @Column(name = "mileage", nullable = false)
    private Integer mileage;


    @Column(name = "cargo_transportation", nullable = false)
    private Integer cargo_transportation;

}