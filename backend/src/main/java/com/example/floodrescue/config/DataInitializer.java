package com.example.floodrescue.config;

import com.example.floodrescue.model.*;
import com.example.floodrescue.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final VehicleRepository vehicleRepository;
    private final ReliefItemRepository reliefItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           RescueTeamRepository rescueTeamRepository,
                           VehicleRepository vehicleRepository,
                           ReliefItemRepository reliefItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.rescueTeamRepository = rescueTeamRepository;
        this.vehicleRepository = vehicleRepository;
        this.reliefItemRepository = reliefItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedTeams();
        seedUsers();
        seedVehicles();
        seedReliefItems();
        upgradePlainTextPasswords();
        ensureTeamAssignments();
    }

    private void seedRoles() {
        List<String> roles = List.of("citizen", "rescue_team", "coordinator", "manager", "admin");
        for (String roleName : roles) {
            roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }

    private void seedUsers() {
        seedUser("citizen1", "123", "citizen");
        seedUser("team1", "123", "rescue_team");
        seedUser("coordinator1", "123", "coordinator");
        seedUser("manager1", "123", "manager");
        seedUser("admin1", "123", "admin");
    }

    private void seedUser(String username, String password, String roleName) {
        userRepository.findByUsername(username).orElseGet(() -> {
            Role role = roleRepository.findByName(roleName).orElseThrow();
            return userRepository.save(new User(username, passwordEncoder.encode(password), role));
        });
    }

    private void seedTeams() {
        if (rescueTeamRepository.count() == 0) {
            rescueTeamRepository.saveAll(List.of(new RescueTeam("Team A"), new RescueTeam("Team B")));
        }
    }

    private void seedVehicles() {
        if (vehicleRepository.count() == 0) {
            vehicleRepository.saveAll(List.of(
                    new Vehicle("Boat 1", "available"),
                    new Vehicle("Truck 1", "available")
            ));
        }
    }

    private void seedReliefItems() {
        if (reliefItemRepository.count() == 0) {
            reliefItemRepository.saveAll(List.of(
                    new ReliefItem("Water", 100),
                    new ReliefItem("Food", 200)
            ));
        }
    }

    private void upgradePlainTextPasswords() {
        userRepository.findAll().forEach(user -> {
            String password = user.getPassword();
            if (password != null && !password.startsWith("$2")) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
        });
    }

    private void ensureTeamAssignments() {
        Role rescueTeamRole = roleRepository.findByName("rescue_team").orElse(null);
        if (rescueTeamRole == null) {
            return;
        }

        List<RescueTeam> teams = rescueTeamRepository.findAll();
        if (teams.isEmpty()) {
            return;
        }

        userRepository.findAll().stream()
                .filter(user -> user.getRole() != null)
                .filter(user -> "rescue_team".equalsIgnoreCase(user.getRole().getName()))
                .filter(user -> user.getTeam() == null)
                .forEach(user -> {
                    RescueTeam assignedTeam = teams.get(0);
                    if ("team1".equalsIgnoreCase(user.getUsername()) && teams.size() >= 1) {
                        assignedTeam = teams.get(0);
                    }
                    user.setTeam(assignedTeam);
                    userRepository.save(user);
                });
    }
}
