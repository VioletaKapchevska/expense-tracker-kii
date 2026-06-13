package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    @OneToOne
    private User user;
    private LocalDateTime expirationDate;

    // TODO: 07.6.2025 expiration to be 5 minutes after the token is generated
    public VerificationToken(String token, User user, LocalDateTime expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expirationDate);
    }
}

