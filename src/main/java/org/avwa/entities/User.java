package org.avwa.entities;

import org.avwa.enums.UserRoleEnum;
import org.avwa.enums.UserTypeEnum;
import org.avwa.system.authentication.oAuth.OAuthProviderType;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Cacheable(false)
@DynamicUpdate(value = true)
@Table(name = "users")
public class User extends EntityBase {

    private String name;

    public String email;

    public String social_id;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private UserTypeEnum type;

    @Enumerated(EnumType.STRING)
    private OAuthProviderType social_type;

    private byte[] passwordHash;

    private byte[] salt;

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public OAuthProviderType getSocial_type() {
        return social_type;
    }

    public void setSocial_type(OAuthProviderType social_type) {
        this.social_type = social_type;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

}
