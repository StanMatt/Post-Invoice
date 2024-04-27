package pl.postinvoice.invoice.accountant;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LeaveClientFromAccountantRequest {

    @NotNull
    Long accountantId;
    @NotNull
    Long clientId;


}
