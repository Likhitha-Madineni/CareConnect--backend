package com.careconnect.service;

import com.careconnect.dto.AuthResponseDto;
import com.careconnect.dto.LoginRequestDto;
import com.careconnect.dto.RegisterRequestDto;
import com.careconnect.dto.UserDto;
import com.careconnect.entity.User;
import com.careconnect.repository.UserRepository;
import com.careconnect.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final ModelMapper modelMapper;

  @Transactional
  public AuthResponseDto register(RegisterRequestDto dto) {
    String email = dto.getEmail().trim().toLowerCase();
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already registered");
    }
    User user =
        User.builder()
            .name(dto.getName().trim())
            .email(email)
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(dto.getRole())
            .build();
    userRepository.save(user);
    return buildAuthResponse(user);
  }

  public AuthResponseDto login(LoginRequestDto dto) {
    String email = dto.getEmail().trim().toLowerCase();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid email or password");
    }
    return buildAuthResponse(user);
  }

  public UserDto toUserDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }

  private AuthResponseDto buildAuthResponse(User user) {
    String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
    return AuthResponseDto.builder()
        .token(token)
        .user(modelMapper.map(user, UserDto.class))
        .build();
  }
}
