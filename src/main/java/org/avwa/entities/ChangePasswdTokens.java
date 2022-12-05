package org.avwa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "change_pswd_tokens")
public class ChangePasswdTokens extends EntityBase {

    public String email;

    public String token;

    private LocalDateTime valid_until_dateTime;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

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

    public LocalDateTime getValid_until_dateTime() {
        return valid_until_dateTime;
    }

    public void setValid_until_dateTime(LocalDateTime valid_until_dateTime) {
        this.valid_until_dateTime = valid_until_dateTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
