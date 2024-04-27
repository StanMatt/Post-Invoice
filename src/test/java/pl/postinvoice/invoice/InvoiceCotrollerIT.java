package pl.postinvoice.invoice;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.postinvoice.Util.BaseIT;
import pl.postinvoice.invoice.detail.InvoiceDetail;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InvoiceCotrollerIT extends BaseIT {


    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreateInvoiceRequest request = new CreateInvoiceRequest(null, null,null);
        // when
        ResultActions resultActions = performPost("/api/invoices", request);

        // then
         resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(3)))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.buyer").value("must not be blank"))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.seller").value("must not be blank"))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.paymentdate").value("must not be null"));
    }
    @Test
    void givenCorrectRequest_whenCreate_thenCreateRequest1() throws Exception {
        // given
        LocalDate paymentdate = LocalDate.now().plusDays(1);
        String buyer = "buyer";
        String seller = "seller";

        CreateInvoiceRequest request = new CreateInvoiceRequest(
                paymentdate,
                buyer,
                seller

        );


        String content = objectMapper.writeValueAsString(request);
        // when
        ResultActions resultActions = performPost("/api/invoices", request);
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        List<Invoice> invoices = entityManager.createQuery("select i from Invoice i left join fetch i.invoiceDetails").getResultList();

        assertThat(invoices).hasSize(1);
        Invoice invoice = invoices.get(0);

        assertThat(invoice)
                .extracting(
                        Invoice::getId,
                        Invoice::getCreatedDateTime,
                        Invoice::getModifiedDateTime
                ).isNotNull();

        assertThat(invoice)
                .extracting(
                        Invoice::getVersion,
                        Invoice::getPaymentdate,
                        Invoice::getBuyer,
                        Invoice::getSeller,
                        Invoice::getStatus
                ).containsExactly(
                        0,
                paymentdate,
                buyer,
                seller,
                invoiceStatus.ACTIVE
                );
        assertThat(invoice.getInvoiceDetails()).hasSize(2);
        assertThat(invoice.getInvoiceDetails())
                .extracting(InvoiceDetail::getProductName)
                .containsExactlyInAnyOrder("Product1", "Product2");


    }

    @Test
    void givenCorrectRequest_whenCreate_thenCreateRequest2() throws Exception {
        // given
        LocalDate paymentdate = LocalDate.now().plusDays(1);
        String buyer = "buyer";
        String seller = "seller";

        CreateInvoiceRequest request = new CreateInvoiceRequest(
                paymentdate,
                buyer,
                seller

        );


        String content = objectMapper.writeValueAsString(request);
        // when
        ResultActions resultActions = performPost("/api/invoices", request);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        List<Invoice> invoices = entityManager.createQuery("select i from Invoice i left join fetch i.invoiceDetails").getResultList();

        assertThat(invoices).hasSize(1);
        Invoice invoice = invoices.get(0);

        assertThat(invoice)
                .extracting(
                        Invoice::getId,
                        Invoice::getCreatedDateTime,
                        Invoice::getModifiedDateTime
                ).isNotNull();

        assertThat(invoice)
                .extracting(
                        Invoice::getVersion,
                        Invoice::getPaymentdate,
                        Invoice::getBuyer,
                        Invoice::getSeller,
                        Invoice::getStatus
                ).containsExactly(
                        0,
                        paymentdate,
                        buyer,
                        seller,
                        invoiceStatus.ACTIVE
                );
        assertThat(invoice.getInvoiceDetails()).hasSize(2);
        assertThat(invoice.getInvoiceDetails())
                .extracting(InvoiceDetail::getProductName)
                .containsExactlyInAnyOrder("Product1", "Product2");


    }

}