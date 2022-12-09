
package org.avwa.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class EntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "InvSeq")
    @SequenceGenerator(name = "InvSeq", sequenceName = "INV_SEQ", allocationSize = 1)
    private Long id;

    private UUID uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        uuid = UUID.randomUUID();
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return (getId() != null)
        ? (getClass().getSimpleName().hashCode() + getId().hashCode())
        : super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && getId() != null
        && other.getClass().isAssignableFrom(getClass())
        && getClass().isAssignableFrom(other.getClass()))
        ? getId().equals(((EntityBase) other).getId())
        : (other == this);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
