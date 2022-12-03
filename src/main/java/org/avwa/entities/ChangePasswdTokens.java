package org.avwa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "change_pswd_tokens")
public class ChangePasswdTokens extends EntityBase {

    public String email;
    
    public String token;

    private LocalDateTime dateTimeIssued;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDateTimeIssued() {
        return dateTimeIssued;
    }

    public void setDateTimeIssued(LocalDateTime dateTimeIssued) {
        this.dateTimeIssued = dateTimeIssued;
    }

}
