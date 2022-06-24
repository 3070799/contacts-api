package com.ohol.pavel.contactsapi.tags;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.ohol.pavel.contactsapi.config.ApplicationProfile.TEST;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Tag("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(TEST)
public @interface IntegrationTest {
}
