package com.ohol.pavel.contactsapi.user.auth.jwtinvalid.repository;

import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.model.InvalidJWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Pavel Ohol
 */
public interface InvalidJWTRepository extends JpaRepository<InvalidJWT, String> {

    Optional<InvalidJWT> findByAccessToken(String accessToken);

    @Transactional
    void deleteByAccessToken(String accessToken);

}
