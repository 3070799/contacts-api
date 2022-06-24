package com.ohol.pavel.contactsapi.contact.service;

import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.model.Contact;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pavel Ohol
 */
public interface ContactService {

    Page<Contact> findAll(Pageable pageable);

    Contact findById(String id);

    Contact create(ContactRequest request, MultipartFile file);

    Contact update(String id, ContactRequest request);

    void deleteById(String id);

    Contact deleteImg(String contactId, String fileName);

    Contact updateImg(String contactId, MultipartFile file);

    ByteArrayResource downloadImg(String fileName);

}
