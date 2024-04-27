package pl.postinvoice.invoice.detail;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.postinvoice.Util.SpecificationUtil;
import pl.postinvoice.invoice.Invoice;
import pl.postinvoice.invoice.InvoiceService;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceService invoiceService;

    @Transactional
    public void create(@Valid CreateInvoiceDetailRequest createInvoiceDetailRequest) {
        Invoice invoice = invoiceService.findInvoiceById(createInvoiceDetailRequest.getInvoiceId());

        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .productName(createInvoiceDetailRequest.getProductName())
                .price(createInvoiceDetailRequest.getPrice())
                .invoice(invoice)
                .build();
        log.debug("Wynik IvoiceDetail: {}", invoiceDetail);
        log.info("Wynik save: {}", invoiceDetailRepository.save(invoiceDetail));
    }

    /*@Transactional(readOnly = true)*/
    public ReadInvoiceDetailResponse findById(Long id) {
        return invoiceDetailRepository.findByIdFetchInvoice(id)
                .map(ReadInvoiceDetailResponse::from)
                .orElseThrow(EntityNotFoundException::new);

    }

    @Transactional
    public void update(Long id, UpdateInvoiceDetailRequest updateInvoiceDetailRequest) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        log.debug("Co tu mam: {}", invoiceDetail);
        InvoiceDetail detail = invoiceDetail.toBuilder()
                .version(updateInvoiceDetailRequest.getVersion())
                .productName(updateInvoiceDetailRequest.getProductName())
                .price(updateInvoiceDetailRequest.getPrice())
                .build();


        InvoiceDetail save = invoiceDetailRepository.save(detail);
        log.info("Zapisane zostało: {}", save);

    }

    public void delete(Long id) {
        invoiceDetailRepository.deleteById(id);
    }

    public Page<ReadInvoiceDetailResponse> find(Long invoiceId, Pageable pageable) {
        return invoiceDetailRepository.findAll(prepareInvoiceSpecification(invoiceId), pageable)
                .map(ReadInvoiceDetailResponse::from);
    }

    private Specification<InvoiceDetail> prepareInvoiceSpecification(Long invoiceId) {
        return (root, query, criteriaBuilder) -> {
            if (!SpecificationUtil.isCountQuery(query)) {
                root.fetch("invoice");
            }
            return criteriaBuilder.equal(root.get("invoice").get("id"), invoiceId);
        };
    }
}
