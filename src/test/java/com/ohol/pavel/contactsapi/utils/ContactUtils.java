package com.ohol.pavel.contactsapi.utils;

import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.model.Contact;

import java.time.OffsetDateTime;

import static com.ohol.pavel.contactsapi.contact.model.ContactGender.MALE;
import static com.ohol.pavel.contactsapi.utils.TestUtils.*;

public class ContactUtils {

    public static ContactRequest randomContactRequest() {
        return ContactRequest.builder()
                .firstName(randomString())
                .lastName(randomString())
                .gender(MALE)
                .age(randomInt())
                .country(randomString())
                .city(randomString())
                .phoneNumber(randomPhoneNumber())
                .build();
    }

    public static Contact randomContact() {
        return Contact.builder()
                .firstName(randomString())
                .lastName(randomString())
                .gender(MALE)
                .age(randomInt())
                .country(randomString())
                .city(randomString())
                .phoneNumber(randomPhoneNumber())
                .createdAt(OffsetDateTime.now())
                .imgUrl(randomString())
                .build();
    }


}
