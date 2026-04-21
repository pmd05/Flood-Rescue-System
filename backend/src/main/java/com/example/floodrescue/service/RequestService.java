package com.example.floodrescue.service;

import com.example.floodrescue.dto.RequestCreateRequest;
import com.example.floodrescue.dto.StatusUpdateRequest;
import com.example.floodrescue.exception.BadRequestException;
import com.example.floodrescue.exception.ResourceNotFoundException;
import com.example.floodrescue.model.RescueRequest;
import com.example.floodrescue.model.User;
import com.example.floodrescue.repository.AssignmentRepository;
import com.example.floodrescue.repository.RescueRequestRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestService {
    private final RescueRequestRepository rescueRequestRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserService userService;

    public RequestService(RescueRequestRepository rescueRequestRepository,
                          AssignmentRepository assignmentRepository,
                          UserService userService) {
        this.rescueRequestRepository = rescueRequestRepository;
        this.assignmentRepository = assignmentRepository;
        this.userService = userService;
    }

    public RescueRequest create(RequestCreateRequest request, String username) {
        User user = userService.findByUsername(username);

        RescueRequest rescueRequest = new RescueRequest();
        rescueRequest.setUser(user);
        rescueRequest.setDescription(request.getDescription());
        rescueRequest.setLatitude(request.getLat());
        rescueRequest.setLongitude(request.getLng());
        rescueRequest.setPriority(normalizePriority(request.getPriority()));
        rescueRequest.setStatus("pending");

        return rescueRequestRepository.save(rescueRequest);
    }

    public List<RescueRequest> getAll(String username, String roleName) {
        String normalizedRole = roleName == null ? "" : roleName.trim().toLowerCase();
        if ("citizen".equals(normalizedRole)) {
            User user = userService.findByUsername(username);
            return rescueRequestRepository.findByUserOrderByCreatedAtDesc(user);
        }
        if ("rescue_team".equals(normalizedRole)) {
            User user = userService.findByUsername(username);
            if (user.getTeam() == null) {
                throw new BadRequestException("Tài khoản rescue_team chưa được gán đội cứu hộ");
            }
            Map<Long, RescueRequest> requests = new LinkedHashMap<>();
            assignmentRepository.findByTeam_IdOrderByAssignedAtDesc(user.getTeam().getId())
                    .forEach(assignment -> {
                        if (assignment.getRequest() != null) {
                            requests.put(assignment.getRequest().getId(), assignment.getRequest());
                        }
                    });
            return requests.values().stream().toList();
        }
        return rescueRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public RescueRequest getById(Long id) {
        return rescueRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy request với id = " + id));
    }

    public RescueRequest getByIdAccessible(Long id, String username, String roleName) {
        RescueRequest rescueRequest = getById(id);
        String normalizedRole = roleName == null ? "" : roleName.trim().toLowerCase();
        if ("citizen".equals(normalizedRole)) {
            User user = userService.findByUsername(username);
            if (rescueRequest.getUser() == null || !rescueRequest.getUser().getId().equals(user.getId())) {
                throw new ResourceNotFoundException("Không tìm thấy request với id = " + id);
            }
        }
        if ("rescue_team".equals(normalizedRole)) {
            User user = userService.findByUsername(username);
            if (user.getTeam() == null) {
                throw new BadRequestException("Tài khoản rescue_team chưa được gán đội cứu hộ");
            }
            boolean belongsToTeam = assignmentRepository.findByTeam_IdOrderByAssignedAtDesc(user.getTeam().getId()).stream()
                    .anyMatch(assignment -> assignment.getRequest() != null && assignment.getRequest().getId().equals(id));
            if (!belongsToTeam) {
                throw new ResourceNotFoundException("Không tìm thấy request với id = " + id);
            }
        }
        return rescueRequest;
    }

    public RescueRequest updateStatus(Long id, StatusUpdateRequest request) {
        throw new BadRequestException("Không hỗ trợ cập nhật trạng thái trực tiếp. Hãy dùng verify, assign hoặc complete task theo đúng nghiệp vụ");
    }

    public RescueRequest verify(Long id) {
        RescueRequest rescueRequest = getById(id);
        if (!"pending".equalsIgnoreCase(rescueRequest.getStatus())) {
            throw new BadRequestException("Chỉ request ở trạng thái pending mới có thể verify");
        }
        rescueRequest.setStatus("verified");
        return rescueRequestRepository.save(rescueRequest);
    }

    public RescueRequest save(RescueRequest rescueRequest) {
        return rescueRequestRepository.save(rescueRequest);
    }

    private String normalizePriority(String priority) {
        String value = priority == null ? "medium" : priority.trim().toLowerCase();
        if (!List.of("low", "medium", "high").contains(value)) {
            throw new BadRequestException("Priority không hợp lệ");
        }
        return value;
    }
}
