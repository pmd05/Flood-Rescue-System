package com.example.floodrescue.service;

import com.example.floodrescue.dto.LoginRequest;
import com.example.floodrescue.dto.LoginResponse;
import com.example.floodrescue.exception.UnauthorizedException;
import com.example.floodrescue.model.User;
import com.example.floodrescue.repository.UserRepository;
import com.example.floodrescue.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Sai username hoặc password"));

        boolean matched;
        String raw = request.getPassword();
        String stored = user.getPassword();
        if (stored != null && stored.startsWith("$2")) {
            matched = passwordEncoder.matches(raw, stored);
        } else {
            matched = raw.equals(stored);
        }

        if (!matched) {
            throw new UnauthorizedException("Sai username hoặc password");
        }

        String role = user.getRole().getName();
        String token = jwtService.generateToken(user.getUsername(), role);
        return new LoginResponse(token, role);
    }
}
