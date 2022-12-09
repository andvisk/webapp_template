package org.avwa.entities;

import org.avwa.enums.AppPropNamesEnum;
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
@Table(name = "app_properties", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "parameter" }) })
public class ApplicationProperties extends EntityBase {

    @Enumerated(EnumType.STRING)
    private AppPropNamesEnum name;

    public String parameter;

    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AppPropNamesEnum getName() {
        return name;
    }

    public void setName(AppPropNamesEnum name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
