package com.tagalong.entity;

import com.tagalong.modal.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "users" )
public class User extends BaseEntity{

    @Column (unique = true)
    private String userUid;

    @Column(nullable= false, unique= true)
    private String email;

    @Column(nullable= false)
    private String password;

    @Column(nullable=false)
    private String fullName;

    public User(String email, String password, String fullName){
        this.email = email;
        this.password = password;
        this.fullName= fullName;
    }

}
