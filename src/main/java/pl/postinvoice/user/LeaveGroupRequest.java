package pl.postinvoice.user;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LeaveGroupRequest {

    @NotNull
    Long userId;
    @NotNull
    Long groupId;
}
