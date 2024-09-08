package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime registrationDate;
    private String userName;
    private String firstName;
    private String lastName;

    public User(LocalDateTime registrationDate, String userName, String firstName, String lastName) {
        this.registrationDate = registrationDate;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
