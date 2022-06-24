package com.ohol.pavel.contactsapi.contact.dto;

import com.ohol.pavel.contactsapi.contact.model.ContactGender;
import com.ohol.pavel.contactsapi.json.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * @author Pavel Ohol
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDTO {

    private String id;

    private String firstName;

    private String lastName;

    private ContactGender gender;

    private Integer age;

    private String country;

    private String city;

    private String phoneNumber;

    private String imgUrl;

    private String imgId;

    @JsonSerialize(using = DateTimeSerializer.class)
    private OffsetDateTime createdAt;

}
