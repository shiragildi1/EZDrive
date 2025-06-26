package com.ezdrive.ezdrive.persistence.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User 
{
    @Id
    @Column(name = "email_id")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "given_name")
    private String  givenName;
    
    @Column(name = "family_name")
    private String familyName;
}
