package com.careconnect.controller;

import com.careconnect.entity.Role;
import com.careconnect.entity.User;
import com.careconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    /**
     * ✅ GET LOGISTICS USERS
     * GET /api/users/logistics
     */
    @GetMapping("/logistics")
    public List<User> getLogisticsUsers() {
        return userRepository.findByRole(Role.LOGISTICS);
    }
}