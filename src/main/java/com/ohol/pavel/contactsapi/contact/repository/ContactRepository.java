package com.ohol.pavel.contactsapi.contact.repository;

import com.ohol.pavel.contactsapi.contact.model.Contact;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Pavel Ohol
 */
public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {



}
