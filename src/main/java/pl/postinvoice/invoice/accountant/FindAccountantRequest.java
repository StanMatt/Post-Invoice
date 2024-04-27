package pl.postinvoice.invoice.accountant;


import jakarta.validation.constraints.NotNull;

public record FindAccountantRequest(@NotNull Long clientId,
                                    String name) {


}
