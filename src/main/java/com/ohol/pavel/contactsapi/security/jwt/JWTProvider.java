package com.ohol.pavel.contactsapi.security.jwt;

import com.ohol.pavel.contactsapi.config.ApplicationSettings;
import com.ohol.pavel.contactsapi.exception.ApplicationException;
import com.ohol.pavel.contactsapi.exception.ErrorMessage;
import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.repository.InvalidJWTRepository;
import com.ohol.pavel.contactsapi.user.model.UserRole;
import com.ohol.pavel.contactsapi.user.service.UserService;
import com.ohol.pavel.contactsapi.exception.ErrorDescription;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

/**
 * @author Pavel Ohol
 */
@Component
@RequiredArgsConstructor
public class JWTProvider {

    private String secretKey;

    private final ApplicationSettings theAppSettings;

    private final UserService theUserService;

    private final InvalidJWTRepository theInvalidJWTRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(theAppSettings.getJwtSettings().getSecret().getBytes());
    }

    public String createToken(String username, UserRole role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("permissions", role.getAuthorities().stream()
                .map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 24 * theAppSettings.getJwtSettings().getValidityInDays());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (theInvalidJWTRepository.findByAccessToken(token).isPresent()) {
                return false;
            }
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApplicationException(ErrorMessage.builder()
                    .reason(ErrorDescription.JWT_NOT_VALID)
                    .build());
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = theUserService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        try {
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

}
