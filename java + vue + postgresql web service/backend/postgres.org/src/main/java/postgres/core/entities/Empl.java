package postgres.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Types;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "empl")
@NoArgsConstructor
@AllArgsConstructor
public class Empl implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fio", nullable = false)
    private String fio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Posts post;

    @Column(name = "headId", nullable = false)
    private Integer headId;

}