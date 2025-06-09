package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezdrive.ezdrive.persistence.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{
    Optional<User> findByEmail(String email); 
}
