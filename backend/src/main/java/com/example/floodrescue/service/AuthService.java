package com.example.floodrescue.service;

import com.example.floodrescue.dto.LoginRequestDTO;
import com.example.floodrescue.dto.LoginResponse;
import com.example.floodrescue.exception.UnauthorizedException;
import com.example.floodrescue.model.AppRole;
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

    public LoginResponse login(LoginRequestDTO request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Sai username hoặc password"));

        // CHUẨN: luôn dùng bcrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Sai username hoặc password");
        }

        // map role sang enum
        AppRole appRole = AppRole.fromDbName(user.getRole().getName());

        // tạo token
        String token = jwtService.generateToken(user.getUsername(), appRole.dbName());

        return new LoginResponse(token, appRole.dbName());
    }
}