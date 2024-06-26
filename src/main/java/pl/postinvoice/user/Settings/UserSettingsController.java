package pl.postinvoice.user.Settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/settings")
@RequiredArgsConstructor
@Slf4j
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @PostMapping
    public void createOrUpdate(@Valid @RequestBody CreateUserSettingsRequest createUserSettingsRequest) {
        userSettingsService.createOrUpdate(createUserSettingsRequest);
    }
}
