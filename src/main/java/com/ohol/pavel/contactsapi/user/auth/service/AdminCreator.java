package com.ohol.pavel.contactsapi.user.auth.service;

import com.ohol.pavel.contactsapi.config.ApplicationSettings;
import com.ohol.pavel.contactsapi.user.model.User;
import com.ohol.pavel.contactsapi.user.repository.UserRepository;
import com.ohol.pavel.contactsapi.user.model.UserRole;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Pavel Ohol
 */
@Component
@AllArgsConstructor
public class AdminCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCreator.class);

    private final ApplicationSettings theAppSettings;

    private final UserRepository theUserRepository;

    private final BCryptPasswordEncoder thePasswordEncoder;

    @PostConstruct
    private void init() {
        String adminLogin = theAppSettings.getAdminCredentials().getLogin();
        String adminPassword = theAppSettings.getAdminCredentials().getPassword();

        if (theUserRepository.findByLogin(adminLogin).isEmpty()) {
            LOGGER.info("Creating admin...");
            theUserRepository.save(User.builder()
                            .login(adminLogin)
                            .password(thePasswordEncoder.encode(adminPassword))
                            .role(UserRole.ADMIN)
                    .build());
            LOGGER.info("Admin created");
        } else {
            LOGGER.info("Admin already exist");
        }
    }

}
