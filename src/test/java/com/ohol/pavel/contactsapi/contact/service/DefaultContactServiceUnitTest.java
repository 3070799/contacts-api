package com.ohol.pavel.contactsapi.contact.service;

import com.ohol.pavel.contactsapi.aws.s3.AmazonS3Service;
import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.model.Contact;
import com.ohol.pavel.contactsapi.contact.repository.ContactRepository;
import com.ohol.pavel.contactsapi.exception.ApplicationException;
import com.ohol.pavel.contactsapi.exception.ErrorDescription;
import com.ohol.pavel.contactsapi.tags.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.ohol.pavel.contactsapi.utils.ContactUtils.randomContactRequest;
import static com.ohol.pavel.contactsapi.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@UnitTest
class DefaultContactServiceUnitTest {

    @Mock
    protected AmazonS3Service s3Service;

    @Mock
    protected ContactRepository theContactRepository;

    @Mock
    protected CacheManager cacheManager;

    protected MultipartFile multipartFile;

    protected DefaultContactService sut;

    protected ContactRequest request;

    @BeforeEach
    void setUp() {
        sut = new DefaultContactService(s3Service, theContactRepository, cacheManager);
        multipartFile = new MockMultipartFile(randomString(), randomString().getBytes());
        request = randomContactRequest();
    }

    @Test
    void findAll() {
        // given
        when(theContactRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(Contact.builder().build())));

        // when
        Page<Contact> result = sut.findAll(Pageable.unpaged());

        // then
        assertThat(result).isNotNull().hasSize(1);
        verify(theContactRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findByIdSuccessfulPath() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .build()));

        // when
        Contact result = sut.findById(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        verify(theContactRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnFind() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void create() {
        // given
        when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn(randomString());
        when(theContactRepository.save(any(Contact.class))).thenAnswer(i -> {
            Contact contact = i.getArgument(0);
            return Contact.builder()
                    .id(randomUUID())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .gender(contact.getGender())
                    .age(contact.getAge())
                    .country(contact.getCountry())
                    .city(contact.getCity())
                    .phoneNumber(contact.getPhoneNumber())
                    .createdAt(contact.getCreatedAt())
                    .imgUrl(contact.getImgUrl())
                    .build();
        });
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

        verify(s3Service, times(1)).uploadFile(any(MultipartFile.class), anyString());
        verify(theContactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void updateSuccessfulPath() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .build()));
        when(theContactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Contact result = sut.update(id, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(result.getLastName()).isEqualTo(request.getLastName());
        assertThat(result.getGender()).isEqualTo(request.getGender());
        assertThat(result.getAge()).isEqualTo(request.getAge());
        assertThat(result.getCountry()).isEqualTo(request.getCountry());
        assertThat(result.getCity()).isEqualTo(request.getCity());
        assertThat(result.getPhoneNumber()).isEqualTo(request.getPhoneNumber());

        verify(theContactRepository, times(1)).findById(id);
        verify(theContactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnUpdateContact() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenAnswer(i -> Optional.empty());

        // when/then
        assertThatThrownBy(() -> sut.update(id, request))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void deleteByIdSuccessfulPath() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .imgUrl(randomUUID())
                .build()));
        doNothing().when(s3Service).deleteFile(anyString());
        doNothing().when(theContactRepository).deleteById(id);

        // when
        sut.deleteById(id);

        // then
        verify(theContactRepository, times(1)).findById(id);
        verify(s3Service, times(1)).deleteFile(anyString());
        verify(theContactRepository).deleteById(id);
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnDeleteContact() {
        // given
        String id = randomUUID();
        when(theContactRepository.findById(id)).thenAnswer(i -> Optional.empty());

        // when/then
        assertThatThrownBy(() -> sut.deleteById(id))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void deleteImgSuccessfulPath() {
        // given
        String contactId = randomUUID();
        String fileName = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .imgUrl(fileName)
                .build()));
        doNothing().when(s3Service).deleteFile(anyString());
        when(theContactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Contact result = sut.deleteImg(contactId, fileName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(contactId);
        assertThat(result.getImgUrl()).isNull();

        verify(theContactRepository, times(1)).findById(contactId);
        verify(s3Service, times(1)).deleteFile(fileName);
        verify(theContactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnDeleteImg() {
        // given
        String contactId = randomUUID();
        String fileName = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.empty());

        // when/then
        assertThatThrownBy(() -> sut.deleteImg(contactId, fileName))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowOnContactWithUrlWasNonExistedOnDeleteImg() {
        // given
        String contactId = randomUUID();
        String fileName = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .imgUrl(randomUUID())
                .build()));

        // when/then
        assertThatThrownBy(() -> sut.deleteImg(contactId, fileName))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.BAD_REQUEST.getMessage());
    }

    @Test
    void updateImgSuccessfulPathIfImgUrlNotNull() {
        // given
        String contactId = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .imgUrl(multipartFile.getName())
                .build()));
        when(s3Service.uploadFile(multipartFile, contactId)).thenAnswer(i -> {
            MultipartFile file = i.getArgument(0);
            return file.getName();
        });
        when(theContactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Contact result = sut.updateImg(contactId, multipartFile);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(contactId);
        assertThat(result.getImgUrl()).isEqualTo(multipartFile.getName());

        verify(theContactRepository, times(1)).findById(contactId);
        verify(s3Service, times(1)).uploadFile(multipartFile, contactId);
        verify(theContactRepository, times(1)).save(any(Contact.class));

    }

    @Test
    void updateImgSuccessfulPathIfImgUrlNull() {
        String contactId = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.of(Contact.builder()
                .id(i.getArgument(0))
                .build()));
        when(s3Service.uploadFile(multipartFile, contactId)).thenAnswer(i -> {
            MultipartFile file = i.getArgument(0);
            return file.getName();
        });
        when(theContactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Contact result = sut.updateImg(contactId, multipartFile);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(contactId);
        assertThat(result.getImgUrl()).isEqualTo(multipartFile.getName());

        verify(theContactRepository, times(1)).findById(contactId);
        verify(s3Service, times(1)).uploadFile(multipartFile, contactId);
        verify(theContactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void shouldThrowOnContactWithIdWasNonExistedOnUpdateImg() {
        String contactId = randomUUID();
        when(theContactRepository.findById(contactId)).thenAnswer(i -> Optional.empty());

        // when/then
        assertThatThrownBy(() -> sut.updateImg(contactId, multipartFile))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ErrorDescription.CONTACT_NOT_FOUND.getMessage());
    }

    @Test
    void downloadImg() {
        // given
        String fileName = randomString();
        byte[] fileBytes = randomString().getBytes();
        when(s3Service.downloadFile(fileName)).thenReturn(fileBytes);

        // when
        ByteArrayResource result = sut.downloadImg(fileName);

        // then
        assertThat(result).isNotNull()
                .extracting(ByteArrayResource::getByteArray)
                .isEqualTo(fileBytes);
        verify(s3Service, times(1)).downloadFile(fileName);
    }

}