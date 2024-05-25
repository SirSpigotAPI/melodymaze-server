package app.melodymaze.server.controller;

import app.melodymaze.server.user.User;
import app.melodymaze.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<User>> findUsers(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

}
