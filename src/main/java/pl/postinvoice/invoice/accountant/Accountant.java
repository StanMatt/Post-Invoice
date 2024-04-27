package pl.postinvoice.invoice.accountant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.invoice.client.Client;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "clients")
@ToString(exclude = "clients")
@Builder(toBuilder = true)
public class Accountant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;

    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @NotNull
    private LocalDateTime modifiedDateTime;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Client> clients;
}
