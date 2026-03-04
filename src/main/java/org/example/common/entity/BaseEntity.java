package org.example.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base entity that all domain entities should extend.
 *
 * Provides common fields that every database table in this application will have:
 * - id:        A UUID primary key. Using UUIDs instead of auto-increment integers
 *              avoids leaking sequential IDs in the API and is safe to generate
 *              client-side if needed.
 * - createdAt: Timestamp automatically set when the entity is first persisted.
 * - updatedAt: Timestamp automatically updated every time the entity is saved.
 *
 * Usage example:
 * <pre>
 *   {@literal @}Entity
 *   {@literal @}Table(name = "notes")
 *   public class Note extends BaseEntity {
 *       private String title;
 *       // ...
 *   }
 * </pre>
 *
 * @MappedSuperclass tells JPA to include these fields in subclass tables
 * but NOT to create a separate table for BaseEntity itself.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * Unique identifier for every entity.
     * Generated as a random UUID before the entity is first persisted.
     * Using {@code @GeneratedValue(strategy = GenerationType.UUID)} delegates
     * generation to JPA, keeping the entity class free of manual UUID creation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * The timestamp when this record was created.
     * Set once by {@link #onCreate()} and never updated after that.
     * The {@code updatable = false} flag enforces this at the JPA level.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when this record was last modified.
     * Automatically refreshed on every save by {@link #onUpdate()}.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback — called automatically before the entity is inserted.
     * Sets both {@code createdAt} and {@code updatedAt} to the current time.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * JPA lifecycle callback — called automatically before the entity is updated.
     * Refreshes {@code updatedAt} to the current time so it always reflects
     * the most recent modification.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

