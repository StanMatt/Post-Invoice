package pl.postinvoice.invoice.detail;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.postinvoice.BaseUnitTest;
import pl.postinvoice.invoice.Invoice;
import pl.postinvoice.invoice.InvoiceService;
import pl.postinvoice.invoice.ReadInvoiceResponse;
import pl.postinvoice.invoice.invoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class InvoiceDetailServiceTest extends BaseUnitTest {
    @InjectMocks
    private InvoiceDetailService underTest;
    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;
    @Mock
    private InvoiceService invoiceService;

    @Test
    void givenInvioceDetailsIdNotExist_whenFindById_thenEntityNotFoundException() {

        // given
        Long expectedInvoiceDetailsId = 12345L;
        when(invoiceDetailRepository.findByIdFetchInvoice(expectedInvoiceDetailsId)).thenReturn(Optional.empty());
        // when

        Executable executable = () -> underTest.findById(expectedInvoiceDetailsId);


        // then

        assertThrows(EntityNotFoundException.class, executable);

    }

    @Test
    void givenInvioceDetailsIdExists_whenFindById_thenReturnResponse() {

        // given

        Long expectedInvoiceDetailsId = 12345L;
        BigDecimal expectedInvoiceDetailsPrice = BigDecimal.TEN;
        String expectedInvoiceDeatailsProductName = "TestowyProdukt";
        LocalDateTime expectedCreatedInvoiceDetailsDateTime = LocalDateTime.of(2024, 12, 21, 11, 41, 21);


        long expectedInvoiceId = 321L;
        int expectedVersionInvoiceAndInvoiceDeatils = 0;
        String expectedInvoiceBuyer = "Kupujący";
        String expectedInvoiceSeller = "Sprzedający";
        LocalDateTime expectedInvoiceCreatedTime = LocalDateTime.of(2024, 12, 12, 10, 11, 21);
        LocalDate expectedInvoicePaymentDate = LocalDate.of(2024, 11, 12);
        invoiceStatus expectedInvoiceStatus = invoiceStatus.ACTIVE;


        Invoice invoice = Invoice.builder()
                .id(expectedInvoiceId)
                .version(expectedVersionInvoiceAndInvoiceDeatils)
                .buyer(expectedInvoiceBuyer)
                .seller(expectedInvoiceSeller)
                .createdDateTime(expectedInvoiceCreatedTime)
                .paymentdate(expectedInvoicePaymentDate)
                .status(expectedInvoiceStatus)
                .build();
        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .id(expectedInvoiceDetailsId)
                .version(expectedVersionInvoiceAndInvoiceDeatils)
                .price(expectedInvoiceDetailsPrice)
                .productName(expectedInvoiceDeatailsProductName)
                .createdDateTime(expectedCreatedInvoiceDetailsDateTime)
                .invoice(invoice)
                .build();

        // when
        when(invoiceDetailRepository.findByIdFetchInvoice(expectedInvoiceDetailsId)).thenReturn(Optional.of(invoiceDetail));
        ReadInvoiceDetailResponse response = underTest.findById(expectedInvoiceDetailsId);


        // then

        assertThat(response).isNotNull();

        assertThat(response).extracting(
                        ReadInvoiceDetailResponse::getId,
                        ReadInvoiceDetailResponse::getVersion,
                        ReadInvoiceDetailResponse::getPrice,
                        ReadInvoiceDetailResponse::getProductName,
                        ReadInvoiceDetailResponse::getCreatedDateTime
                )
                .containsExactly(
                        expectedInvoiceDetailsId,
                        expectedVersionInvoiceAndInvoiceDeatils,
                        expectedInvoiceDetailsPrice,
                        expectedInvoiceDeatailsProductName,
                        expectedCreatedInvoiceDetailsDateTime
                );


        ReadInvoiceResponse invoiceResponse = response.getInvoice();
        assertThat(invoiceResponse).isNotNull();
        assertThat(invoiceResponse)
                .extracting(ReadInvoiceResponse::id,
                        ReadInvoiceResponse::version,
                        ReadInvoiceResponse::buyer,
                        ReadInvoiceResponse::seller,
                        ReadInvoiceResponse::createdDateTime,
                        ReadInvoiceResponse::paymentdate,
                        ReadInvoiceResponse::status)
                .containsExactly(
                        expectedInvoiceId,
                        expectedVersionInvoiceAndInvoiceDeatils,
                        expectedInvoiceBuyer,
                        expectedInvoiceSeller,
                        expectedInvoiceCreatedTime,
                        expectedInvoicePaymentDate,
                        expectedInvoiceStatus
                );
    }
}