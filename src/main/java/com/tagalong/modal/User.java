package com.tagalong.modal;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable= false, unique= true)
    private String email;
    
    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String fullName;

    @Column(nullable=false, updatable= false)
    private LocalDateTime createdAt;

    public User(String email, String password, String fullName){
        this.email = email;
        this.password = password;
        this.fullName= fullName;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
