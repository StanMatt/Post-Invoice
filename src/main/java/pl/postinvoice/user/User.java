package pl.postinvoice.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.user.adress.Address;
import pl.postinvoice.groupInfo.GroupInfo;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@ToString(exclude = {"groupsInfo", "address"})
@EqualsAndHashCode(exclude = {"groupsInfo", "address"})
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;


    @NotNull
    @Size(max = 100)
    private String login;

    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @NotNull
    private LocalDateTime lastModifiedDateTime;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<GroupInfo> groupsInfo;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Address address;
}
