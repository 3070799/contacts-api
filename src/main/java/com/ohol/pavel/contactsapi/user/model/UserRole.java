package com.ohol.pavel.contactsapi.user.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ohol.pavel.contactsapi.user.model.UserPermission.CONTACT_READ;
import static com.ohol.pavel.contactsapi.user.model.UserPermission.CONTACT_WRITE;

/**
 * @author Pavel Ohol
 */
@AllArgsConstructor
public enum UserRole {

    ADMIN(
            Set.of(
                    CONTACT_READ,
                    CONTACT_WRITE
            )
    ),

    USER(
            Set.of(
                    CONTACT_READ
            )
    );

    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream().map(
                p -> new SimpleGrantedAuthority(p.getPermission())
        ).collect(Collectors.toSet());
    }

}
