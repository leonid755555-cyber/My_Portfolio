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
@Table(name = "transport")
@NoArgsConstructor
@AllArgsConstructor
public class Transport implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "typeId", nullable = false)
    private Types type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placeId", nullable = false)
    private Places place;

    @Column(name = "dt", nullable = false)
    private LocalDate dt1;

    @Column(name = "working_life", nullable = false)
    private LocalDate working_life;
}