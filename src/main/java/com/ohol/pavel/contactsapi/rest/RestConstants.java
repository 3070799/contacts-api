package com.ohol.pavel.contactsapi.rest;

/**
 * @author Pavel Ohol
 */
public interface RestConstants {

    String ROOT = "/api";

    String API_VERSION = "/v1";

    interface Auth {

        String AUTH_ROOT = ROOT + API_VERSION + "/auth";

        String LOGIN = AUTH_ROOT + "/login";

        String LOGOUT = AUTH_ROOT + "/logout";

        String CHECK = AUTH_ROOT + "/check";

        String REGISTER_ROOT = AUTH_ROOT + "/register";

        String REGISTER_USER = REGISTER_ROOT + "/user";
    }

    interface User {

        String USER_ROOT = ROOT + API_VERSION + "/users";

        String ME = USER_ROOT + "/me";

    }

    interface Contact {

        String CONTACTS_ROOT = ROOT + API_VERSION + "/contacts";

        String CONTACT = CONTACTS_ROOT + "/{contactId}";

        String CONTACTS_IMAGES_ROOT = CONTACTS_ROOT + "/images";

        String CONTACTS_IMAGES_CONTACT = CONTACTS_IMAGES_ROOT + "/{contactId}";

        String CONTACT_IMAGE = CONTACTS_IMAGES_ROOT + "/{contactId}/{fileName}";

        String CONTACTS_IMAGE_FILE = CONTACTS_IMAGES_ROOT + "/{fileName}";

        String UPDATE_CONTACT_AND_IMAGE = ROOT + API_VERSION + "/contact-image/{contactId}";

    }

}
