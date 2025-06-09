package com.ezdrive.ezdrive.persistence.Entities;

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
    private String email;
    private String name;
    private String picture;
    private String googleId;
    private boolean emailVerified;
    private String  givenName;
    private String familyName;
}
