package com.ezdrive.ezdrive.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.UserRepository;

//Service to handle users
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) 
    {
        return userRepository.findByEmail(email);
    }
    
    public User createUser(User user) 
    {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent())
        {
            System.out.println("User alredy exsist");
            return null;
        }
        return userRepository.save(user);
    }
}
