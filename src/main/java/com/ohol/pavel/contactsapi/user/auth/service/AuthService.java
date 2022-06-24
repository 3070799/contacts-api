package com.ohol.pavel.contactsapi.user.auth.service;

import com.ohol.pavel.contactsapi.user.dto.AuthDTO;
import com.ohol.pavel.contactsapi.user.dto.LoginRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pavel Ohol
 */
public interface AuthService {

    AuthDTO login(LoginRequest request);

    void logout(String login, HttpServletRequest request);

    AuthDTO check(String login, HttpServletRequest request);
}
