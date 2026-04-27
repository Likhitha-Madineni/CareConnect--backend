package com.careconnect.controller;

import com.careconnect.dto.AuthResponseDto;
import com.careconnect.dto.LoginRequestDto;
import com.careconnect.dto.RegisterRequestDto;
import com.careconnect.dto.UserDto;
import com.careconnect.entity.User;
import com.careconnect.security.UserPrincipal;
import com.careconnect.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto body) {
    return authService.register(body);
  }

  @PostMapping("/login")
  public AuthResponseDto login(@Valid @RequestBody LoginRequestDto body) {
    return authService.login(body);
  }

  @GetMapping("/me")
  public UserDto me(@AuthenticationPrincipal UserPrincipal principal) {
    User user = principal.getUser();
    return authService.toUserDto(user);
  }
}
