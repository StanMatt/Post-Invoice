package pl.postinvoice.invoice.detail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class CreateInvoiceDetailRequest {


    @NotBlank
    @Size(max = 200)
    String productName;

    @NotNull
    BigDecimal price;
    @NotNull
    Long invoiceId;


}

