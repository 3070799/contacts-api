package com.ohol.pavel.contactsapi.user.service;

import com.ohol.pavel.contactsapi.exception.ApplicationException;
import com.ohol.pavel.contactsapi.exception.ErrorMessage;
import com.ohol.pavel.contactsapi.security.CustomUserDetails;
import com.ohol.pavel.contactsapi.user.dto.RegisterRequest;
import com.ohol.pavel.contactsapi.user.model.User;
import com.ohol.pavel.contactsapi.user.model.UserRole;
import com.ohol.pavel.contactsapi.user.repository.UserRepository;
import com.ohol.pavel.contactsapi.exception.ErrorDescription;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Pavel Ohol
 */
@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    private final ConversionService theConversionService;

    private final UserRepository theUserRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request, UserRole role) {
        if (theUserRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new ApplicationException(
                    ErrorMessage.builder()
                            .reason(ErrorDescription.USER_ALREADY_EXIST)
                            .build());
        }

        return theUserRepository.save(User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build());
    }

    @Override
    public User findByLogin(String login) {
        return theUserRepository.findByLogin(login).orElseThrow(() ->
                new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.USER_NOT_FOUND)
                        .build())
        );
    }

    @Override
    public User findById(String id) {
        return theUserRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.USER_NOT_FOUND)
                        .build())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return theConversionService.convert(theUserRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("Username not found")), CustomUserDetails.class);
    }
}
