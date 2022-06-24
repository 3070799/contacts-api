package com.ohol.pavel.contactsapi.user.converter;

import com.ohol.pavel.contactsapi.user.dto.UserDTO;
import com.ohol.pavel.contactsapi.user.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Pavel Ohol
 */
@Component
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        return UserDTO.builder()
                .id(source.getId())
                .login(source.getLogin())
                .role(source.getRole())
                .build();
    }
}
