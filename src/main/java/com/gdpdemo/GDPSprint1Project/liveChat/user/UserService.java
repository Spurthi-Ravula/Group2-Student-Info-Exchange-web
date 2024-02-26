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
        User user1 = repository.findByEmail(user.email);
        return repository.save(user1);
    }

    public User disconnect(AddUser user) {
        User storedUser = repository.findByEmail(user.email);
        return storedUser;
    }

    public List<User> findConnectedUsers() {
        return repository.findAll();
    }
}
