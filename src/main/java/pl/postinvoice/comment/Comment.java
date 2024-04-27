package pl.postinvoice.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.post.Post;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@ToString(exclude = {"post"})
@EqualsAndHashCode(exclude = {"post"})
@Builder(toBuilder = true)
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(max = 5000)
    private String text;
    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;
    @LastModifiedDate
    @NotNull

    private LocalDateTime lastModifiedDateTime;


    @NotBlank
    @NotNull
    @Size(max = 100)
    private String author;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Post post;

}
