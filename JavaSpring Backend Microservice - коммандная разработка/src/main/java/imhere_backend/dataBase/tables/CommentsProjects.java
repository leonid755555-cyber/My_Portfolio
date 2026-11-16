package imhere_backend.dataBase.tables;
import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments_projects")
public class CommentsProjects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_project", nullable = false)
    private Projects project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private UserProfiles userProfile;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date_created", nullable = false)
    private LocalDate date_created;

    @Column(name = "count_likes", nullable = false)
    private Integer count_likes;
}