package com.ohol.pavel.contactsapi.user.auth.controller;

import com.ohol.pavel.contactsapi.security.CustomUserDetails;
import com.ohol.pavel.contactsapi.user.dto.AuthDTO;
import com.ohol.pavel.contactsapi.user.dto.LoginRequest;
import com.ohol.pavel.contactsapi.user.dto.RegisterRequest;
import com.ohol.pavel.contactsapi.user.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pavel Ohol
 */
public interface AuthController {

    ResponseEntity<UserDTO> register(RegisterRequest request);

    ResponseEntity<AuthDTO> login(LoginRequest request);

    void logout(CustomUserDetails userDetails, HttpServletRequest request);

    ResponseEntity<AuthDTO> check(CustomUserDetails userDetails, HttpServletRequest request);
}
