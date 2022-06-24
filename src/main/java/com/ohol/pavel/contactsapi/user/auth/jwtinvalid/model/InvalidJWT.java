package com.ohol.pavel.contactsapi.user.auth.jwtinvalid.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Pavel Ohol
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invalid_jwts")
public class InvalidJWT {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid2")
    @GeneratedValue(generator = "generator")
    private String id;

    @Column(name = "access_token")
    private String accessToken;

}
