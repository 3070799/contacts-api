package com.ohol.pavel.contactsapi.contact.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * @author Pavel Ohol
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid2")
    @GeneratedValue(generator = "generator")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private ContactGender gender;

    private Integer age;

    private String country;

    private String city;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "img_url")
    private String imgUrl;

}
