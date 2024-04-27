package pl.postinvoice.invoice.detail;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.invoice.Invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"invoice"})
@ToString(exclude = {"invoice"})
@Builder(toBuilder = true)
public class InvoiceDetail {
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
    private String productName;

    @NotNull
    private BigDecimal price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Invoice invoice;


}
