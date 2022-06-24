package com.ohol.pavel.contactsapi.user.dto;

import lombok.*;

/**
 * @author Pavel Ohol
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {

    private String accessToken;

    private UserDTO user;

}
