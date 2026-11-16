package imhere_backend.dataBase.tables;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tags")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tag_name", nullable = false)
    private String tag_name;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tag_category", nullable = false)
    private TagCategories tagCategory;

}