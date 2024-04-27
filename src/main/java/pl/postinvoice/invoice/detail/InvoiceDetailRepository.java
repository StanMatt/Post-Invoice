package pl.postinvoice.invoice.detail;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvoiceDetailRepository extends CrudRepository<InvoiceDetail, Long>, JpaSpecificationExecutor<InvoiceDetail> {

    @Query("select inv from InvoiceDetail inv join fetch inv.invoice where inv.id=:id")
    Optional<InvoiceDetail> findByIdFetchInvoice(Long id);

}
