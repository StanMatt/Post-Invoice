package pl.postinvoice.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateCommentRequest {

    @NotNull
    @Size(max = 5000)
    String text;

    @NotNull
    @NotBlank
    @Size(max = 100)
    String author;


}
