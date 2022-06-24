package com.ohol.pavel.contactsapi.contact.service;

import com.ohol.pavel.contactsapi.aws.s3.AmazonS3Service;
import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.model.Contact;
import com.ohol.pavel.contactsapi.contact.repository.ContactRepository;
import com.ohol.pavel.contactsapi.exception.ApplicationException;
import com.ohol.pavel.contactsapi.exception.ErrorMessage;
import com.ohol.pavel.contactsapi.exception.ErrorDescription;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.ohol.pavel.contactsapi.config.cache.CacheNames.IMAGES_CACHE;
import static java.util.Objects.nonNull;

/**
 * @author Pavel Ohol
 */
@Service
@AllArgsConstructor
public class DefaultContactService implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContactService.class);

    private final AmazonS3Service s3Service;

    private final ContactRepository theContactRepository;

    private final CacheManager cacheManager;

    @Override
    public Page<Contact> findAll(Pageable pageable) {
        return theContactRepository.findAll(pageable);
    }

    @Override
    public Contact findById(String id) {
        return theContactRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.CONTACT_NOT_FOUND)
                        .build())
        );
    }

    @Override
    public Contact create(ContactRequest request, MultipartFile file) {
        Contact newContact = Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .age(request.getAge())
                .country(request.getCountry())
                .city(request.getCity())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(OffsetDateTime.now())
                .build();
        newContact.setImgUrl(s3Service.uploadFile(file, UUID.randomUUID().toString()));
        return theContactRepository.save(newContact);
    }

    @Override
    public Contact update(String id, ContactRequest request) {
        Contact updatedContact = theContactRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.CONTACT_NOT_FOUND)
                        .build())
        );

        updatedContact.setFirstName(request.getFirstName());
        updatedContact.setLastName(request.getLastName());
        updatedContact.setGender(request.getGender());
        updatedContact.setAge(request.getAge());
        updatedContact.setCountry(request.getCountry());
        updatedContact.setCity(request.getCity());
        updatedContact.setPhoneNumber(request.getPhoneNumber());

        return theContactRepository.save(updatedContact);
    }

    @Override
    public void deleteById(String id) {
        Contact contact = theContactRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.CONTACT_NOT_FOUND)
                        .build())
        );

        s3Service.deleteFile(contact.getImgUrl());
        Optional.ofNullable(cacheManager.getCache(IMAGES_CACHE))
                .ifPresent(c -> c.evictIfPresent(contact.getImgUrl()));

        theContactRepository.deleteById(id);
    }

    @Override
    public Contact deleteImg(String contactId, String fileName) {
        Contact contact = theContactRepository.findById(contactId).orElseThrow(
                () -> new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.CONTACT_NOT_FOUND)
                        .build())
        );

        if (!contact.getImgUrl().equals(fileName)) {
            throw new ApplicationException(ErrorMessage.builder()
                    .reason(ErrorDescription.BAD_REQUEST)
                    .build());
        }

        s3Service.deleteFile(fileName);
        Optional.ofNullable(cacheManager.getCache(IMAGES_CACHE))
                .ifPresent(c -> c.evictIfPresent(contact.getImgUrl()));

        contact.setImgUrl(null);

        return theContactRepository.save(contact);
    }

    @Override
    public Contact updateImg(String contactId, MultipartFile file) {
        Contact contact = theContactRepository.findById(contactId).orElseThrow(
                () -> new ApplicationException(ErrorMessage.builder()
                        .reason(ErrorDescription.CONTACT_NOT_FOUND)
                        .build())
        );

        if (nonNull(contact.getImgUrl())) {
            s3Service.deleteFile(contact.getImgUrl());
            Optional.ofNullable(cacheManager.getCache(IMAGES_CACHE))
                    .ifPresent(c -> c.evictIfPresent(contact.getImgUrl()));
        }

        contact.setImgUrl(s3Service.uploadFile(file, contactId));

        return theContactRepository.save(contact);
    }

    @Override
    @Cacheable(value = IMAGES_CACHE, key = "#fileName")
    public ByteArrayResource downloadImg(String fileName) {
        LOGGER.info("Loading img: {}", fileName);
        return new ByteArrayResource(s3Service.downloadFile(fileName));
    }

}
