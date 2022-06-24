package com.ohol.pavel.contactsapi.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pavel Ohol
 */
@Getter
@AllArgsConstructor
public enum UserPermission {

    CONTACT_READ("contact:read"),
    CONTACT_WRITE("contact:write");

    private final String permission;

}
