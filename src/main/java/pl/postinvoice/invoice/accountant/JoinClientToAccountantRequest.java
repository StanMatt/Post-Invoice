package pl.postinvoice.invoice.accountant;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class JoinClientToAccountantRequest {

    @NotNull
    Long accountantId;
    @NotNull
    Long clientId;


}
