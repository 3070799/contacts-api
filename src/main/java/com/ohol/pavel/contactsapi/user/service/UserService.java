package com.ohol.pavel.contactsapi.user.service;

import com.ohol.pavel.contactsapi.user.dto.RegisterRequest;
import com.ohol.pavel.contactsapi.user.model.User;
import com.ohol.pavel.contactsapi.user.model.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Pavel Ohol
 */
public interface UserService extends UserDetailsService {

    User register(RegisterRequest request, UserRole role);

    User findByLogin(String login);

    User findById(String id);

}
