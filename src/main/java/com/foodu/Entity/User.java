package com.foodu.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String createdAt;

    public enum Role {
        GENERAL, EVENT_MANAGER, TRUCK_OWNER
    }
}