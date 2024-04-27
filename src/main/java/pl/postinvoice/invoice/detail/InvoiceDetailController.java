package pl.postinvoice.invoice.detail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices-details")
@RequiredArgsConstructor
public class InvoiceDetailController {

    private final InvoiceDetailService invoiceDetailService;

    @PostMapping
    public void create(@Valid @RequestBody CreateInvoiceDetailRequest createInvoiceDetailRequest) {
        invoiceDetailService.create(createInvoiceDetailRequest);

    }

    @GetMapping("{id}")
    public ResponseEntity<ReadInvoiceDetailResponse> read(@PathVariable("id") Long id) {
        ReadInvoiceDetailResponse byId = invoiceDetailService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateInvoiceDetailRequest updateInvoiceDetailRequest) {
        invoiceDetailService.update(id, updateInvoiceDetailRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        invoiceDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ReadInvoiceDetailResponse>> find(@RequestParam(value = "inid") Long invoiceId, Pageable pageable) {
        return ResponseEntity.ok(invoiceDetailService.find(invoiceId, pageable));
    }
}
