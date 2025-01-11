package com.example.filekeep.services;

import com.example.filekeep.models.User;
import com.example.filekeep.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

   public List<User> findAll(){
        return this.userRepository.findAll();
   }
}
