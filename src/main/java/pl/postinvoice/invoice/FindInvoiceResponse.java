package pl.postinvoice.invoice;

import java.time.LocalDate;


public record FindInvoiceResponse(Long id, LocalDate paymentdate, String buyer, String seller) {

    public static FindInvoiceResponse from(Invoice invoice) {

        return new FindInvoiceResponse(
                invoice.getId(),
                invoice.getPaymentdate(),
                invoice.getBuyer(),
                invoice.getSeller()
        );
    }

    @Override
    public String toString() {
        return "ReadInvoiceResponse[" +
                "id=" + id + ", " +
                "paymentdate=" + paymentdate + ", " +
                "buyer=" + buyer + ", " +
                "seller=" + seller + ", ";
    }


}
