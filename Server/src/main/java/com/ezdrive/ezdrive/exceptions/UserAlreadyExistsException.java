package com.ezdrive.ezdrive.exceptions;

public class UserAlreadyExistsException extends RuntimeException
{
    public UserAlreadyExistsException() 
    {
        super("User already exists");
    }  
}
