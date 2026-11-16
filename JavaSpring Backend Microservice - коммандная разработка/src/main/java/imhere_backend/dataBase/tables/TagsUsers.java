package imhere_backend.dataBase.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tags_users")
public class TagsUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user_profile", nullable = false)
    private UserProfiles userProfile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tag", nullable = false)
    private Tags tag;
}