package com.tagalong.modal;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity extends JacksonMergeable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "uid")
    private String uid;

    @CreationTimestamp
    @Column(name = "created_at",
        nullable = false,
        updatable = false,
        columnDefinition = "timestamptz"
    )
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at", columnDefinition = "timestamptz")
    private Instant lastUpdateAt;

    @Column(name = "is_deleted", columnDefinition = "bool")
    private Boolean isDeleted = Boolean.FALSE;


    public BaseEntity(Long id, String uid) {
        this.id = id;
        this.uid = uid;
    }

}
