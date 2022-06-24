package com.ohol.pavel.contactsapi.security.converter;

import com.ohol.pavel.contactsapi.security.CustomUserDetails;
import com.ohol.pavel.contactsapi.user.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Pavel Ohol
 */
@Component
public class UserToCustomUserDetailsConverter implements Converter<User, CustomUserDetails> {
    @Override
    public CustomUserDetails convert(User source) {
        return CustomUserDetails.builder()
                .username(source.getLogin())
                .password(source.getPassword())
                .authorities(source.getRole().getAuthorities())
                .build();
    }
}
