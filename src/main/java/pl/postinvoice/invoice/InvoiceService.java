package pl.postinvoice.invoice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.postinvoice.Util.LogUtil;
import pl.postinvoice.invoice.detail.InvoiceDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;


@Service
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public ReadInvoiceResponse findById(Long id) {
        return invoiceRepository.findAllByIdFetchInvoiceDetails(id)
                .map(ReadInvoiceResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }


    @Transactional
    public void create(CreateInvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice(
                invoiceRequest.getPaymentdate(),
                invoiceRequest.getBuyer(),
                invoiceRequest.getSeller()
        );

        Set<InvoiceDetail> invoiceDetails = new HashSet<>();
        invoiceDetails.add(InvoiceDetail.builder().productName("Product1").price(BigDecimal.ONE).invoice(invoice).build());
        invoiceDetails.add(InvoiceDetail.builder().productName("Product2").price(new BigDecimal("22.11")).invoice(invoice).build());
        invoice.setInvoiceDetails(invoiceDetails);


        invoiceRepository.save(invoice);
    }

    @Transactional
    public void update(Long id, UpdateInvoiceRequest updateInvoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Invoice newInvoice = new Invoice(invoice);

        newInvoice.setPaymentdate(updateInvoiceRequest.getPaymentdate());
        newInvoice.setBuyer(updateInvoiceRequest.getBuyer());
        newInvoice.setSeller(updateInvoiceRequest.getSeller());
        newInvoice.setVersion(updateInvoiceRequest.getVersion());

        Set<InvoiceDetail> invoiceDetails = newInvoice.getInvoiceDetails();
        InvoiceDetail nexted = invoiceDetails.iterator().next();
        nexted.setInvoice(null);
        log.info("usuwam InvoiceDetail o id = {}", nexted.getId());
        invoiceDetails.remove(nexted);

        invoiceDetails.add(InvoiceDetail.builder().productName("ProduktUpdate").price(BigDecimal.TEN).invoice(newInvoice).build());


        invoiceRepository.save(newInvoice);

    }

    @Transactional
    public void deleted(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Transactional
    public void archive(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Invoice newInvoice = new Invoice(invoice);
        newInvoice.setStatus(invoiceStatus.DELETED);

        invoiceRepository.save(newInvoice);
    }

    public Page<FindInvoiceResponse> find(String sellerContaining, String buyerContaining, int page, int size) {
        return invoiceRepository.findBySellerContainingAndBuyerContainingAndStatusInOrderByPaymentdate(
                        sellerContaining,
                        buyerContaining,
                        Set.of(invoiceStatus.ACTIVE, invoiceStatus.DRAFT),
                        PageRequest.of(page, size)

                )
                .map(FindInvoiceResponse::from);


    }

    public Page<FindInvoiceResponse> find(FindInvoicesRequest findInvoicesRequest, Pageable pageable) {
        Specification<Invoice> specification = getInvoiceSpecification(findInvoicesRequest);


        return invoiceRepository.findAll(specification, pageable)
                .map(FindInvoiceResponse::from);


    }

    private static Specification<Invoice> getInvoiceSpecification(FindInvoicesRequest findInvoicesRequest) {
        Specification<Invoice> specification = Specification.where(null);

        if (findInvoicesRequest.paymentdateStart() != null && findInvoicesRequest.paymentdateEnd() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("paymentdate"),
                            findInvoicesRequest.paymentdateStart(),
                            findInvoicesRequest.paymentdateEnd()));

        }
        if (findInvoicesRequest.text() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    likeIngoreCase(criteriaBuilder, root.get("seller"), findInvoicesRequest.text()));

        }
        if (findInvoicesRequest.invoiceStatus() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("status").in(findInvoicesRequest.invoiceStatus()));

        }

        return specification;

    }

    private static Specification<Invoice> getInvoiceSpecificationPredicate(FindInvoicesRequest findInvoicesRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (findInvoicesRequest.paymentdateStart() != null && findInvoicesRequest.paymentdateEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("paymentdate"),
                        findInvoicesRequest.paymentdateStart(),
                        findInvoicesRequest.paymentdateEnd()));
            }
            if (findInvoicesRequest.text() != null) {
                predicates.add(likeIngoreCase(criteriaBuilder, root.get("seller"), findInvoicesRequest.text()));
            }
            if (findInvoicesRequest.invoiceStatus() != null) {
                predicates.add(root.get("status").in(findInvoicesRequest.invoiceStatus()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }

    private static Predicate likeIngoreCase(CriteriaBuilder criteriaBuilder, Path<String> fieldPath, String text) {
        return criteriaBuilder.like(criteriaBuilder.lower(fieldPath),
                "%" + text.toLowerCase() + "%");
    }

    public void find2() {


        log(() -> invoiceRepository.find(LocalDate.of(2023, 12, 18)), "find");


        log(() -> invoiceRepository.FindSamemu(LocalDate.of(2024, 01, 01),
                LocalDate.of(2025, 01, 01),
                "Ros",
                Set.of(invoiceStatus.ACTIVE)), "FindByDuzo");

        LogUtil.logPage(() -> invoiceRepository.findAllByPaymentdate(
                        LocalDate.of(2023, 12, 18),
                        PageRequest.of(0, 3, Sort.by(Sort.Order.desc("seller")))),
                "findByPage"
        );
        LogUtil.logPage(() -> invoiceRepository.findAllByPaymentdate(
                        LocalDate.of(2023, 12, 18),
                        PageRequest.of(1, 3, Sort.by(Sort.Order.desc("seller")))),
                "findByPage"
        );
        LogUtil.logPage(() -> invoiceRepository.findAllByPaymentdate(
                        LocalDate.of(2023, 12, 18),
                        PageRequest.of(2, 3, Sort.by(Sort.Order.desc("seller")))),
                "findByPage"
        );
        LogUtil.logPage(() -> invoiceRepository.findAllByPaymentdate(
                        LocalDate.of(2023, 12, 18),
                        PageRequest.of(3, 3, Sort.by(Sort.Order.desc("seller")))),
                "findByPage"
        );
        LogUtil.logPage(() -> invoiceRepository.findAllByPaymentdate(
                        LocalDate.of(2023, 12, 18),
                        PageRequest.of(4, 3, Sort.by(Sort.Order.desc("seller")))),
                "findByPage"
        );


    }

    private void log(Supplier<List<Invoice>> invoiceList, String methodName) {

        System.out.println("*****************************" + methodName + "********************************");

        invoiceList.get().forEach(System.out::println);

    }

}
