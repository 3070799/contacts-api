package com.ohol.pavel.contactsapi.user.dto;

import com.ohol.pavel.contactsapi.user.model.UserRole;
import lombok.*;

/**
 * @author Pavel Ohol
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String login;

    private UserRole role;

}
