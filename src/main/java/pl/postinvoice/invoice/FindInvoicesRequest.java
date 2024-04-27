package pl.postinvoice.invoice;

import java.time.LocalDate;
import java.util.Set;

public record FindInvoicesRequest(LocalDate paymentdateStart,
                                  LocalDate paymentdateEnd,
                                  String text,
                                  Set<invoiceStatus> invoiceStatus) {
}
