package com.ohol.pavel.contactsapi.user.auth.jwtinvalid;

import com.ohol.pavel.contactsapi.config.ApplicationSettings;
import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.model.InvalidJWT;
import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.repository.InvalidJWTRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Ohol
 */
@Service
@AllArgsConstructor
public class InvalidJWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidJWTService.class);

    private final ApplicationSettings theAppSettings;

    private final InvalidJWTRepository theInvalidJWTRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void cleanupInvalidJwts() {

        LOGGER.info("Run scheduled job: CLEANUP INVALID JWTS");

        List<String> invalidTokens = theInvalidJWTRepository.findAll().stream()
                .map(InvalidJWT::getAccessToken).collect(Collectors.toList());

        invalidTokens.stream().filter(this::validateToken)
                .forEach(theInvalidJWTRepository::deleteByAccessToken);
    }

    private boolean validateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(
                    theAppSettings.getJwtSettings().getSecret().getBytes())
                    ).parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

}
