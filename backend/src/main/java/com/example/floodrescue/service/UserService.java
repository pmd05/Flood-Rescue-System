package com.example.floodrescue.service;

import com.example.floodrescue.dto.UserCreateRequest;
import com.example.floodrescue.dto.UserUpdateRequest;
import com.example.floodrescue.exception.BadRequestException;
import com.example.floodrescue.exception.ResourceNotFoundException;
import com.example.floodrescue.model.RescueTeam;
import com.example.floodrescue.model.Role;
import com.example.floodrescue.model.User;
import com.example.floodrescue.repository.AssignmentRepository;
import com.example.floodrescue.repository.RescueRequestRepository;
import com.example.floodrescue.repository.RescueTeamRepository;
import com.example.floodrescue.repository.RoleRepository;
import com.example.floodrescue.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final RescueRequestRepository rescueRequestRepository;
    private final AssignmentRepository assignmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       RescueTeamRepository rescueTeamRepository,
                       RescueRequestRepository rescueRequestRepository,
                       AssignmentRepository assignmentRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rescueTeamRepository = rescueTeamRepository;
        this.rescueRequestRepository = rescueRequestRepository;
        this.assignmentRepository = assignmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username đã tồn tại");
        }

        Role role = findRole(request.getRole());

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        applyTeamByRole(user, role.getName(), request.getTeamId(), false);

        return userRepository.save(user);
    }

    public User update(Long id, UserUpdateRequest request) {
        User user = findById(id);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        String nextRole = user.getRole().getName();
        if (request.getRole() != null && !request.getRole().isBlank()) {
            Role role = findRole(request.getRole());
            user.setRole(role);
            nextRole = role.getName();
        }

        applyTeamByRole(user, nextRole, request.getTeamId(), true);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        if (rescueRequestRepository.existsByUser_Id(user.getId())) {
            throw new BadRequestException("Không thể xóa user đã tạo rescue request");
        }
        if (user.getTeam() != null && assignmentRepository.existsByTeam_Id(user.getTeam().getId())) {
            throw new BadRequestException("Không thể xóa rescue team user vì đội đã có lịch sử phân công");
        }
        userRepository.delete(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + id));
    }

    private Role findRole(String roleName) {
        return roleRepository.findByName(roleName.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy role: " + roleName));
    }

    private void applyTeamByRole(User user, String roleName, Long requestedTeamId, boolean isUpdate) {
        String normalizedRole = roleName == null ? "" : roleName.trim().toLowerCase();
        if (!"rescue_team".equals(normalizedRole)) {
            user.setTeam(null);
            return;
        }

        Long effectiveTeamId = requestedTeamId;
        if (effectiveTeamId == null && isUpdate && user.getTeam() != null) {
            effectiveTeamId = user.getTeam().getId();
        }

        if (effectiveTeamId == null) {
            throw new BadRequestException("User role rescue_team phải được gán vào một đội cứu hộ");
        }

        RescueTeam team = rescueTeamRepository.findById(effectiveTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rescue team"));
        user.setTeam(team);
    }
}
