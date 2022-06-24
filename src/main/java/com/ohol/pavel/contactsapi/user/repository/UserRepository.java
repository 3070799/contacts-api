package com.ohol.pavel.contactsapi.user.repository;

import com.ohol.pavel.contactsapi.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Pavel Ohol
 */
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByLogin(String login);

}
