package com.ohol.pavel.contactsapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Pavel Ohol
 */
@ConfigurationProperties(prefix = "contacts", ignoreUnknownFields = false)
@Getter
public final class ApplicationSettings {

    private final JwtSettings jwtSettings = new JwtSettings();

    private final AdminCredentials adminCredentials = new AdminCredentials();

    private final ImageSettings imageSettings = new ImageSettings();

    @Getter
    @Setter
    public static class AdminCredentials {

        private String login;

        private String password;

    }

    @Getter
    @Setter
    public static class JwtSettings {

        private String secret;

        private long validityInDays;

    }

    @Getter
    @Setter
    public static class ImageSettings {

        private String serverUrl;

    }

}
