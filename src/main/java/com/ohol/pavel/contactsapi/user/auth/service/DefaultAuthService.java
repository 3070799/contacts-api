package com.ohol.pavel.contactsapi.user.auth.service;

import com.ohol.pavel.contactsapi.security.jwt.JWTProvider;
import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.model.InvalidJWT;
import com.ohol.pavel.contactsapi.user.auth.jwtinvalid.repository.InvalidJWTRepository;
import com.ohol.pavel.contactsapi.user.dto.AuthDTO;
import com.ohol.pavel.contactsapi.user.dto.LoginRequest;
import com.ohol.pavel.contactsapi.user.dto.UserDTO;
import com.ohol.pavel.contactsapi.user.model.User;
import com.ohol.pavel.contactsapi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pavel Ohol
 */
@Service
@AllArgsConstructor
public class DefaultAuthService implements AuthService {

    private final ConversionService theConversionService;

    private final AuthenticationManager theAuthenticationManager;

    private final UserService theUserService;

    private final JWTProvider theJwtProvider;

    private final InvalidJWTRepository theInvalidJWTRepository;

    @Override
    public AuthDTO login(LoginRequest request) {
        authenticate(request.getLogin(), request.getPassword());
        User user = theUserService.findByLogin(request.getLogin());
        String accessToken = authorize(user);
        return AuthDTO.builder()
                .accessToken(accessToken)
                .user(theConversionService.convert(user, UserDTO.class))
                .build();
    }

    @Override
    public void logout(String login, HttpServletRequest request) {
        String token = theJwtProvider.resolveToken(request);

        theInvalidJWTRepository.save(InvalidJWT.builder()
                .accessToken(token)
                .build());
    }

    @Override
    public AuthDTO check(String login, HttpServletRequest request) {
        String token = theJwtProvider.resolveToken(request);
        User user = theUserService.findByLogin(login);

        return AuthDTO.builder()
                .accessToken(token)
                .user(theConversionService.convert(user, UserDTO.class))
                .build();
    }

    private void authenticate(String username, String password) {
        theAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private String authorize(User user) {
        return theJwtProvider.createToken(user.getLogin(), user.getRole());
    }


}
