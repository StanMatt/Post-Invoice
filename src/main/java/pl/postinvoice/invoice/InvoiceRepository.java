package pl.postinvoice.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InvoiceRepository extends CrudRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    List<Invoice> findAllByPaymentdateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(LocalDate startDate, LocalDate endDate, String Seller, Set<invoiceStatus> statuses);

    @Query("select ind from Invoice ind left join fetch ind.invoiceDetails where ind.id=:id")
    Optional<Invoice> findAllByIdFetchInvoiceDetails(Long id);

    @Query("select i from Invoice i where i.paymentdate <= :localDate order by i.paymentdate desc")
    List<Invoice> find(LocalDate localDate);

    List<Invoice> findByStatusInAndSellerContaining(Set<invoiceStatus> statuses, String seller);

    @Query("select i from Invoice i where i.paymentdate <= :localdate")
    List<Invoice> findby(LocalDate localdate, Sort sort);

    List<Invoice> findByPaymentdateLessThanEqual(LocalDate startDate, Sort sort);

    Page<Invoice> findByPaymentdate(LocalDate localDate, Pageable pageable);


    @Query("select i from Invoice i where i.paymentdate <= :paymentdate")
    Page<Invoice> findAllByPaymentdate(LocalDate paymentdate, Pageable pageable);

    @Query("select i from Invoice i where i.paymentdate between :startDate  and :endDate " +
            " and i.seller ilike :seller% " +
            "and i.status in :statuses ")
    List<Invoice> findByDuzo(LocalDate startDate, LocalDate endDate, String seller, Set<invoiceStatus> statuses);


    Page<Invoice> findBySellerContainingAndBuyerContainingAndStatusInOrderByPaymentdate(
            String seller,
            String buyer,
            Set<invoiceStatus> invoiceStatuses,
            Pageable pageable
    );

    @Query("select i from Invoice i where i.paymentdate between :startDate and :endDate " +
            "and i.seller ilike :seller% " +
            "and i.status in :statuses ")
    List<Invoice> FindSamemu(LocalDate startDate, LocalDate endDate, String seller, Set<invoiceStatus> statuses);


}
