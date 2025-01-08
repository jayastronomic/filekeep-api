package com.example.filekeep.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

/**
 * The ApplicationEntity class serves as a base entity for all entities in the application,
 * providing common fields in functionality such as unique identifiers and timestamps
 * @param <T> The type of payload that can be used to update this entity.
 */
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Will autogenerate timestamps
@EntityListeners(AuditingEntityListener.class)
public abstract class ApplicationEntity<T> {
    /**
     * Unique identifier for each entity in the application. For clarity, user cannot update id, id is unique, id cannot be null
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;

    /**
     * Timestamp indicating when entity was created
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    /**
     * Timestamp indicating when entity was last updated
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public ApplicationEntity(UUID id) {
        this.id = id;
    }
}