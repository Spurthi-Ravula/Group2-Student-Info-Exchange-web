package com.example.SMS.liveChat.user;

import com.example.SMS.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload AddUser user
    ) {
        return userService.saveUser(user);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload AddUser user
    ) {
        return userService.disconnect(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
