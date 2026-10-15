package io.quazr.backend.Security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

public class HybridJwtDecoder implements JwtDecoder {
    private final AuxiliaryJwtService auxiliaryJwtDecoder;
    private final JwtDecoder keycloakDecoder;

    public HybridJwtDecoder(JwtDecoder keycloakDecoder, AuxiliaryJwtService auxiliaryJwtDecoder) {
        this.auxiliaryJwtDecoder = auxiliaryJwtDecoder;
        this.keycloakDecoder = keycloakDecoder;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        // Step 1: Attempt Keycloak RS256 Verification
        try {
            return this.keycloakDecoder.decode(token);
        } catch (JwtException keycloakException) {
            // Step 2: Fallback to Local HMAC (Temporary Guest Token) Verification
            try {
                return this.auxiliaryJwtDecoder.decode(token);
            } catch (Exception hmacException) {
                throw new JwtException("Invalid authentication token (Failed both Keycloak and Guest verification)", hmacException);
            }
        }
    }
}