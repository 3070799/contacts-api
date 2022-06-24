package com.ohol.pavel.contactsapi.contact.dto;

import com.ohol.pavel.contactsapi.contact.model.ContactGender;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Pavel Ohol
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequest {

    @ApiModelProperty("Contact first name.")
    private String firstName;

    @ApiModelProperty("Contact last name.")
    private String lastName;

    @ApiModelProperty("Contact gender.")
    private ContactGender gender;

    @ApiModelProperty("Contact age.")
    private Integer age;

    @ApiModelProperty("Contact country.")
    private String country;

    @ApiModelProperty("Contact city.")
    private String city;

    @ApiModelProperty("Contact phone number.")
    private String phoneNumber;

}
