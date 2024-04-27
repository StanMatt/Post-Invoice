package pl.postinvoice.user.Settings;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class CreateUserSettingsRequest {

    @NotNull
    Long userId;

    @NotNull
    Boolean showPanel;

    @NotNull
    Boolean darkMode;


}
