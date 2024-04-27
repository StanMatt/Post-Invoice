package pl.postinvoice.invoice.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class CreateClientRequest {

    @NotNull
    @NotBlank
    @Size(max = 100)
    String name;


}
