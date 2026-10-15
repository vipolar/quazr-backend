package io.quazr.backend.Security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Map;

@Service
public class SecurityService {
    private AuxiliaryJwtService auxiliaryJwtService;

    public SecurityService(AuxiliaryJwtService auxiliaryJwtService) {
        this.auxiliaryJwtService = auxiliaryJwtService;
    }

    public UUID extractUserUUID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }

        if (principal instanceof Jwt jwt) {
            return UUID.fromString(jwt.getSubject());
        }

        return null;
    }

    public Map<String, Object> generateTemporaryToken(UUID userUUID) {
        return this.auxiliaryJwtService.generate(userUUID);
    }
}
