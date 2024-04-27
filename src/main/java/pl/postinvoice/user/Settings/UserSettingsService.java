package pl.postinvoice.user.Settings;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.postinvoice.user.User;
import pl.postinvoice.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserService userService;

    @Transactional
    public void createOrUpdate(CreateUserSettingsRequest createUserSettingsRequest) {
        User user = userService.findById(createUserSettingsRequest.getUserId());
        UserSettings userSettings = userSettingsRepository.findById(createUserSettingsRequest.getUserId())
                .orElse(UserSettings.builder()
                        .user(user)
                        .build()
                );
        userSettings.setShowPanel(createUserSettingsRequest.getShowPanel());
        userSettings.setDarkMode(createUserSettingsRequest.getDarkMode());
        userSettingsRepository.save(userSettings);
    }
}


