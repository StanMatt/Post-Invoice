package pl.postinvoice.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UpdatePostRequest {

    @NotNull
    private final Integer version;
    @NotNull
    @Size(max = 5000)
    private final String text;
    @NotNull
    private final postScope scopecope;


    public UpdatePostRequest(@NotNull Integer version, String text, postScope scopecope) {
        this.version = version;
        this.text = text;
        this.scopecope = scopecope;

    }

    public String getText() {
        return text;
    }

    public postScope getScopecope() {
        return scopecope;
    }

    public Integer getVersion() {
        return version;
    }
}
