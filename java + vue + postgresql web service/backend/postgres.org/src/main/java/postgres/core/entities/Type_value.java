package postgres.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "type_value")
@NoArgsConstructor
@AllArgsConstructor
public class Type_value implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportId", nullable = false)
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propId", nullable = false)
    private Type_properties prop;

    @Column(name = "value", nullable = false)
    private String value;

}