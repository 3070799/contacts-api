package com.ohol.pavel.contactsapi.contact.controller;

import com.ohol.pavel.contactsapi.contact.dto.ContactDTO;
import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pavel Ohol
 */
public interface ContactController {

    ResponseEntity<Page<ContactDTO>> findAll(Pageable pageable);

    ResponseEntity<ContactDTO> findById(String id);

    ResponseEntity<ContactDTO> save(ContactRequest request, MultipartFile file);

    ResponseEntity<ContactDTO> update(String contactId, ContactRequest request);

    ResponseEntity<ContactDTO> updateContactAndImage(String contactId, MultipartFile file, ContactRequest request);

    void delete(String contactId);

    ResponseEntity<ByteArrayResource> downloadImg(String fileName);

    ResponseEntity<ContactDTO> updateImg(String contactId, MultipartFile file);

    ResponseEntity<ContactDTO> deleteImg(String contactId, String fileName);


}
