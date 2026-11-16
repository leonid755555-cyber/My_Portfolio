package postgres.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "repair_parts")
@NoArgsConstructor
@AllArgsConstructor
public class Repair_parts implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rrId", nullable = false)
    private Repair_reports repair_reports;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partId", nullable = false)
    private Parts part;

    @Column(name = "count", nullable = false)
    private Integer count;

}