package com.ohol.pavel.contactsapi.contact.converter;

import com.ohol.pavel.contactsapi.config.ApplicationSettings;
import com.ohol.pavel.contactsapi.contact.dto.ContactDTO;
import com.ohol.pavel.contactsapi.contact.model.Contact;
import com.ohol.pavel.contactsapi.rest.RestConstants;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Pavel Ohol
 */
@Component
@AllArgsConstructor
public class ContactToContactDTOConverter implements Converter<Contact, ContactDTO> {

    private final ApplicationSettings theAppSettings;

    @Override
    public ContactDTO convert(Contact source) {
        return ContactDTO.builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .gender(source.getGender())
                .age(source.getAge())
                .country(source.getCountry())
                .city(source.getCity())
                .phoneNumber(source.getPhoneNumber())
                .imgUrl(getFullImgPath(source.getImgUrl()))
                .imgId(source.getImgUrl())
                .createdAt(source.getCreatedAt())
                .build();
    }

    private String getFullImgPath(String fileName) {
        return theAppSettings.getImageSettings().getServerUrl() + RestConstants.Contact.CONTACTS_IMAGES_ROOT + "/" + fileName;
    }

}
