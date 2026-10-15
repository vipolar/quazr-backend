package io.quazr.backend.Users.Settings;

import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;
    
    public UserSettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }
}
