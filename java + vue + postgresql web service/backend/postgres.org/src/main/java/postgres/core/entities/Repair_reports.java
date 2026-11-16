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
@Table(name = "repair_reports")
@NoArgsConstructor
@AllArgsConstructor
public class Repair_reports implements BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_of_receipt", nullable = false)
    private LocalDate date_of_receipt;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completion_date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportId", nullable = false)
    private Transport transport;

    @Column(name = "work_type", nullable = false)
    private String work_type;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emplId", nullable = false)
    private Empl empl;

}