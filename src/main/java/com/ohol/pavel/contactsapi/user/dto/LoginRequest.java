package com.ohol.pavel.contactsapi.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Pavel Ohol
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @ApiModelProperty("Login.")
    private String login;

    @ApiModelProperty("Password.")
    private String password;

}
