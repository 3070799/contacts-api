package com.ohol.pavel.contactsapi.contact.service;

import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.model.Contact;
import com.ohol.pavel.contactsapi.contact.repository.ContactRepository;
import com.ohol.pavel.contactsapi.exception.ApplicationException;
import com.ohol.pavel.contactsapi.exception.ErrorDescription;
import com.ohol.pavel.contactsapi.tags.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.ohol.pavel.contactsapi.config.cache.CacheNames.IMAGES_CACHE;
import static com.ohol.pavel.contactsapi.utils.ContactUtils.randomContactRequest;
import static com.ohol.pavel.contactsapi.utils.TestUtils.*;
import static com.ohol.pavel.contactsapi.utils.ContactUtils.randomContact;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@IntegrationTest
@SpringBootTest
public class ContactServiceIntegrationTest {


    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ContactRepository theContactRepository;

    @Autowired
    private DefaultContactService sut;

    private ContactRequest request;

    private Contact dbContact1;

    private Contact dbContact2;

    private MultipartFile multipartFile;

    @BeforeEach
    void beforeEach() {
        request = randomContactRequest();
        dbContact1 = theContactRepository.save(randomContact());
        dbContact2 = theContactRepository.save(randomContact());
        multipartFile = new MockMultipartFile(
                randomString(),
                randomString(),
                TEXT_PLAIN_VALUE,
                randomString().getBytes()
        );
    }

    @AfterEach
    void tearDown() {
        theContactRepository.deleteAll();
    }

    @Test
    void findAll() {
        // when
        Page<Contact> result = sut.findAll(Pageable.unpaged());
        // then
        assertThat(result).isNotNull().hasSize(2);
    }

    @Test
    void findByIdSuccessfulPath() {
        // when
        Contact result = sut.findById(dbContact1.getId());
        // then
        assertThat(result).isEqualTo(dbContact1);
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnFind() {
        // when/then
        assertThatThrownBy(() -> sut.findById(randomUUID()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void create() {
        // when
        Contact result = sut.create(request, multipartFile);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(result.getLastName()).isEqualTo(request.getLastName());
        assertThat(result.getGender()).isEqualTo(request.getGender());
        assertThat(result.getAge()).isEqualTo(request.getAge());
        assertThat(result.getCountry()).isEqualTo(request.getCountry());
        assertThat(result.getCity()).isEqualTo(request.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
    }

    @Test
    void updateSuccessfulPath() {
        // when
        Contact result = sut.update(dbContact1.getId(), request);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dbContact1.getId());
        assertThat(result.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(result.getLastName()).isEqualTo(request.getLastName());
        assertThat(result.getGender()).isEqualTo(request.getGender());
        assertThat(result.getAge()).isEqualTo(request.getAge());
        assertThat(result.getCountry()).isEqualTo(request.getCountry());
        assertThat(result.getCity()).isEqualTo(request.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnUpdateContact() {
        // when/ then
        assertThatThrownBy(() -> sut.update(randomUUID(), request))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void deleteByIdSuccessfulPath() {
        // when
        sut.deleteById(dbContact1.getId());
        // then
        assertThat(theContactRepository.findAll()).hasSize(1);
        assertThat(theContactRepository.findById(dbContact1.getId())).isEmpty();
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnDeleteContact() {
        // when/ then
        assertThatThrownBy(() -> sut.deleteById(randomUUID()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void deleteImgSuccessfulPath() {
        // when
        Contact result = sut.deleteImg(dbContact1.getId(), dbContact1.getImgUrl());
        // then
        assertThat(result.getImgUrl()).isNull();
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnDeleteImg() {
        // when/then
        assertThatThrownBy(() -> sut.deleteImg(randomUUID(), randomString()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowOnContactWithUrlWasNonExistedOnDeleteImg() {
        // when/then
        assertThatThrownBy(() -> sut.deleteImg(dbContact2.getId(), randomString()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.BAD_REQUEST.getMessage());
    }

    @Test
    void updateImgSuccessfulPathIfImgUrlNotNull() {
        // when
        Contact result = sut.updateImg(dbContact1.getId(), multipartFile);
        // then
        assertThat(result).isNotNull();
        assertThat(result)
                .isNotNull()
                .extracting(Contact::getId)
                .isEqualTo(dbContact1.getId());
    }

    @Test
    void updateImgSuccessfulPathIfImgUrlNull() {
        //given
        Contact contact = dbContact1;
        contact.setImgUrl(null);
        theContactRepository.save(contact);
        // when
        Contact result = sut.updateImg(dbContact1.getId(), multipartFile);
        // then
        assertThat(result)
                .isNotNull()
                .extracting(Contact::getId)
                .isEqualTo(dbContact1.getId());
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnUpdateImg() {
        // when/then
        assertThatThrownBy(() -> sut.updateImg(randomUUID(), multipartFile))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void downloadImg() {
        // given
        String fileName = randomString();


        // when
        ByteArrayResource result = sut.downloadImg(fileName);
        // then
        assertThat(result).isNotNull()
                .isEqualTo(getCachedImage(fileName));
    }

    private ByteArrayResource getCachedImage(String fileName) {
        return ofNullable(cacheManager.getCache(IMAGES_CACHE))
                .map(c -> c.get(fileName, ByteArrayResource.class))
                .orElse(null);
    }

}

