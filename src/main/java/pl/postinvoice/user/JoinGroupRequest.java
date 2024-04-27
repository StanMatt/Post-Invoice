package pl.postinvoice.user;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class JoinGroupRequest {

    @NotNull
    Long userId;
    @NotNull
    Long groupId;
}
