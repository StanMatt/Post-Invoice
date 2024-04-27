package pl.postinvoice.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateInvoiceRequest {
    @NotNull
    private final Integer version;

    @FutureOrPresent
    private final LocalDate paymentdate;
    @NotBlank
    @Size(max = 200)
    private final String buyer;
    @NotBlank
    @Size(max = 200)
    private final String seller;


    private UpdateInvoiceRequest(Integer version, LocalDate paymentdate, String buyer, String seller) {
        this.version = version;
        this.paymentdate = paymentdate;
        this.buyer = buyer;
        this.seller = seller;

    }

    public Integer getVersion() {
        return version;
    }

    public LocalDate getPaymentdate() {
        return paymentdate;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }


}

