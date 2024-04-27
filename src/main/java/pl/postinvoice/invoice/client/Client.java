package pl.postinvoice.invoice.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.invoice.accountant.Accountant;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "accountants")
@ToString(exclude = "accountants")
@Builder(toBuilder = true)
public class Client {
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clients")
    private Set<Accountant> accountants;
}
