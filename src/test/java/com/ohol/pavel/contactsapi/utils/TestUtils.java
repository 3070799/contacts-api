package com.ohol.pavel.contactsapi.utils;

import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.*;

public class TestUtils {

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String randomString() {
        return randomAlphabetic(20);
    }

    public static String randomPhoneNumber() {
        return String.format("+%s", randomNumeric(12));
    }

    public static Long randomLong() {
        return new Random().nextLong();
    }

    public static int randomInt() {
        return new Random().nextInt();
    }
}
