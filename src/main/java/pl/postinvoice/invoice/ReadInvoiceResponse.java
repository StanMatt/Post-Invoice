package pl.postinvoice.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record ReadInvoiceResponse(Long id, Integer version, LocalDateTime createdDateTime, LocalDate paymentdate,
                                  String buyer, String seller, invoiceStatus status) {

    public static ReadInvoiceResponse from(Invoice invoice) {

        return new ReadInvoiceResponse(
                invoice.getId(),
                invoice.getVersion(),
                invoice.getCreatedDateTime(),
                invoice.getPaymentdate(),
                invoice.getBuyer(),
                invoice.getSeller(),
                invoice.getStatus());
    }

    @Override
    public String toString() {
        return "ReadInvoiceResponse[" +
                "id=" + id + ", " +
                "createdDateTime=" + createdDateTime + ", " +
                "paymentdate=" + paymentdate + ", " +
                "buyer=" + buyer + ", " +
                "seller=" + seller + ", " +
                "status=" + status + ']';
    }


    }

