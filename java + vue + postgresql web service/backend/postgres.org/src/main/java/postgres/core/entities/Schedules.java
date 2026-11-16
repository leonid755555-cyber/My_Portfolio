package postgres.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "schedules")
@NoArgsConstructor
@AllArgsConstructor
public class Schedules implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportId", nullable = false)
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marshrutId", nullable = false)
    private Marshs marshrut;

}