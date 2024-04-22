package com.example.SMS.liveChat.user;


import com.example.SMS.entity.User;
import com.example.SMS.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User saveUser(AddUser user) {
        User storedUser = repository.findByEmail(user.email);
        if(storedUser != null ) {
       	 storedUser.setStatus(Status.ONLINE);
            repository.save(storedUser);
       }
       return storedUser;
    }

    public User disconnect(AddUser user) {
        User storedUser = repository.findByEmail(user.email);
        if(storedUser != null ) {
        	 storedUser.setStatus(Status.OFFLINE);
             repository.save(storedUser);
        }
        return storedUser;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }
}
