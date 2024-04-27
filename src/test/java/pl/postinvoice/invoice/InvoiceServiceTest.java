package pl.postinvoice.invoice;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.postinvoice.BaseUnitTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InvoiceServiceTest extends BaseUnitTest {

    @InjectMocks
    private InvoiceService underTest;
    @Mock
    private InvoiceRepository invoiceRepository;


    @Test
    void givenNoResult_whenFindAll_thenReturnEmptyPage() {

        // given
        FindInvoicesRequest request = new FindInvoicesRequest(null, null, null, null);
        int expectedPageSize = 10;
        Pageable pageable = Pageable.ofSize(expectedPageSize);

        // when
        when(invoiceRepository.findAll(any(), eq(pageable))).thenReturn(Page.empty(pageable));
        Page<FindInvoiceResponse> response = underTest.find(request, pageable);

        // then

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getSize()).isEqualTo(expectedPageSize);

    }

    @Test
    void givenTwoResult_whenFindAll_thenReturnResponseInCorrectOrder() {

        // given
        FindInvoicesRequest request = new FindInvoicesRequest(
                LocalDate.of(2024, 5, 12),
                LocalDate.of(2024, 6, 11),
                "Sp. z o.o. Argon",
                Set.of(invoiceStatus.ACTIVE));
        int expectedPageSize = 10;
        long invoice1Id = 1L;
        long invoice2Id = 2L;

        Pageable pageable = Pageable.ofSize(expectedPageSize);
        Invoice invoice1 = Invoice.builder()
                .id(invoice1Id)
                .invoiceDetails(Set.of())
                .build();
        Invoice invoice2 = Invoice.builder()
                .id(invoice2Id)
                .invoiceDetails(Set.of())
                .build();
        List<Invoice> invoiceList = List.of(invoice1, invoice2);

        when(invoiceRepository.findAll(any(), eq(pageable))).thenReturn(new PageImpl<>(
                invoiceList, pageable, invoiceList.size()
        ));

        // when
        Page<FindInvoiceResponse> response = underTest.find(request, pageable);

        // then

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent())
                .extracting(FindInvoiceResponse::id)
                .containsExactly(invoice1Id, invoice2Id);
        assertThat(response.getSize()).isEqualTo(expectedPageSize);

    }
}