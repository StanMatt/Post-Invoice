package pl.postinvoice.invoice;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.invoice.detail.InvoiceDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
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
    @FutureOrPresent
    private LocalDate paymentdate;
    @NotNull
    @NotBlank
    @Size(max = 200)
    private String buyer;
    @NotNull
    @NotBlank
    @Size(max = 200)
    @NotAudited
    private String seller;
    @NotNull
    @Enumerated(EnumType.STRING)
    private invoiceStatus status;
    @OneToMany(mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<InvoiceDetail> invoiceDetails;


    public Invoice(LocalDate paymentdate, String buyer, String seller) {


        this.paymentdate = paymentdate;
        this.buyer = buyer;
        this.seller = seller;
        this.status = invoiceStatus.ACTIVE;
    }


    public Invoice(Invoice old) {
        this.id = old.id;
        this.version = old.version;
        this.modifiedDateTime = old.modifiedDateTime;
        this.createdDateTime = old.createdDateTime;
        this.paymentdate = old.paymentdate;
        this.buyer = old.buyer;
        this.seller = old.seller;
        this.status = old.status;
        this.invoiceDetails = old.invoiceDetails;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", version=" + version +
                ", modifiedDateTime=" + modifiedDateTime +
                ", createdDateTime=" + createdDateTime +
                ", paymentdate=" + paymentdate +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                ", status=" + status +
                '}';
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setPaymentdate(LocalDate paymentdate) {
        this.paymentdate = paymentdate;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setStatus(invoiceStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }


    public LocalDate getPaymentdate() {
        return paymentdate;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public invoiceStatus getStatus() {
        return status;
    }

    public Set<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(Set<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }
}
