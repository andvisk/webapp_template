package org.avwa.entities;

import org.avwa.enums.OAuthPropertiesNamesEnum;
import org.avwa.system.authentication.oAuth.OAuthProviderType;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Cacheable(false)
@DynamicUpdate(value = true)
@Table(name = "oauth_properties", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "provider" }) })
public class OAuthProperties extends EntityBase {

    @Enumerated(EnumType.STRING)
    private OAuthPropertiesNamesEnum name;

    public String value;

    @Enumerated(EnumType.STRING)
    private OAuthProviderType provider;

    public OAuthPropertiesNamesEnum getName() {
        return name;
    }

    public void setName(OAuthPropertiesNamesEnum name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OAuthProviderType getProvider() {
        return provider;
    }

    public void setProvider(OAuthProviderType provider) {
        this.provider = provider;
    }

}
