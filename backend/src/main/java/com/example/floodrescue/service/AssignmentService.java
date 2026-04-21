package com.example.floodrescue.service;

import com.example.floodrescue.dto.AssignmentCreateRequest;
import com.example.floodrescue.exception.BadRequestException;
import com.example.floodrescue.exception.ResourceNotFoundException;
import com.example.floodrescue.model.Assignment;
import com.example.floodrescue.model.RescueRequest;
import com.example.floodrescue.model.RescueTeam;
import com.example.floodrescue.model.User;
import com.example.floodrescue.model.Vehicle;
import com.example.floodrescue.repository.AssignmentRepository;
import com.example.floodrescue.repository.RescueTeamRepository;
import com.example.floodrescue.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {
    private static final String STATUS_ASSIGNED = "assigned";
    private static final String STATUS_COMPLETED = "completed";
    private static final String STATUS_REVOKED = "revoked";
    private static final List<String> ACTIVE_STATUSES = List.of("assigned", "blocked", "need_backup");

    private static final String VEHICLE_AVAILABLE = "available";
    private static final String VEHICLE_IN_USE = "in_use";

    private final AssignmentRepository assignmentRepository;
    private final RequestService requestService;
    private final UserService userService;
    private final RescueTeamRepository rescueTeamRepository;
    private final VehicleRepository vehicleRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             RequestService requestService,
                             UserService userService,
                             RescueTeamRepository rescueTeamRepository,
                             VehicleRepository vehicleRepository) {
        this.assignmentRepository = assignmentRepository;
        this.requestService = requestService;
        this.userService = userService;
        this.rescueTeamRepository = rescueTeamRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Assignment create(AssignmentCreateRequest request) {
        RescueRequest rescueRequest = requestService.getById(request.getRequestId());
        RescueTeam rescueTeam = rescueTeamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rescue team"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vehicle"));

        if (!"verified".equalsIgnoreCase(rescueRequest.getStatus())) {
            throw new BadRequestException("Request phải ở trạng thái verified mới có thể assign");
        }
        if (!VEHICLE_AVAILABLE.equalsIgnoreCase(vehicle.getStatus())) {
            throw new BadRequestException("Vehicle phải ở trạng thái available mới có thể phân công");
        }
        if (assignmentRepository.existsByRequest_IdAndStatusIgnoreCase(rescueRequest.getId(), STATUS_ASSIGNED)) {
            throw new BadRequestException("Request này đã có lệnh điều phối đang hoạt động");
        }
        if (assignmentRepository.existsByVehicle_IdAndStatusIn(vehicle.getId(), ACTIVE_STATUSES)) {
            throw new BadRequestException("Vehicle đang được sử dụng ở nhiệm vụ khác");
        }
        if (assignmentRepository.existsByTeam_IdAndStatusIn(rescueTeam.getId(), ACTIVE_STATUSES)) {
            throw new BadRequestException("Đội cứu hộ đang bận xử lý nhiệm vụ khác");
        }

        Assignment assignment = new Assignment();
        assignment.setRequest(rescueRequest);
        assignment.setTeam(rescueTeam);
        assignment.setVehicle(vehicle);
        assignment.setStatus(STATUS_ASSIGNED);

        rescueRequest.setStatus(STATUS_ASSIGNED);
        vehicle.setStatus(VEHICLE_IN_USE);

        requestService.save(rescueRequest);
        vehicleRepository.save(vehicle);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAll() {
        return assignmentRepository.findAllByOrderByAssignedAtDesc();
    }

    public List<Assignment> getTeamTasks(String username) {
        User user = userService.findByUsername(username);
        if (user.getTeam() == null) {
            throw new BadRequestException("Tài khoản rescue_team chưa được gán đội cứu hộ");
        }
        return assignmentRepository.findByTeam_IdOrderByAssignedAtDesc(user.getTeam().getId());
    }

    @Transactional
    public Assignment completeTask(Long assignmentId, String username) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy assignment"));
        User user = userService.findByUsername(username);

        if (user.getTeam() == null) {
            throw new BadRequestException("Tài khoản rescue_team chưa được gán đội cứu hộ");
        }
        if (assignment.getTeam() == null || !assignment.getTeam().getId().equals(user.getTeam().getId())) {
            throw new BadRequestException("Bạn chỉ có thể hoàn thành nhiệm vụ của chính đội mình");
        }
        if (!ACTIVE_STATUSES.contains(assignment.getStatus())) {
            throw new BadRequestException("Chỉ assignment đang thực thi mới có thể complete");
        }

        assignment.setStatus(STATUS_COMPLETED);
        assignment.getRequest().setStatus(STATUS_COMPLETED);
        assignment.getVehicle().setStatus(VEHICLE_AVAILABLE);

        vehicleRepository.save(assignment.getVehicle());
        requestService.save(assignment.getRequest());
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment reportException(Long assignmentId, String username, com.example.floodrescue.dto.AssignmentExceptionRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy assignment"));
        User user = userService.findByUsername(username);

        if (user.getTeam() == null) {
            throw new BadRequestException("Tài khoản rescue_team chưa được gán đội cứu hộ");
        }
        if (assignment.getTeam() == null || !assignment.getTeam().getId().equals(user.getTeam().getId())) {
            throw new BadRequestException("Bạn chỉ có thể báo cáo sự cố cho nhiệm vụ của đội mình");
        }
        if (!STATUS_ASSIGNED.equalsIgnoreCase(assignment.getStatus())) {
            throw new BadRequestException("Chỉ nhiệm vụ đang thực thi mới có thể báo sự cố");
        }

        assignment.setStatus(request.getStatus());
        assignment.setExceptionNote(request.getNote());
        assignment.setExceptionImage(request.getImage());

        assignment.getRequest().setStatus(request.getStatus());

        requestService.save(assignment.getRequest());
        return assignmentRepository.save(assignment);
    }

    // 🔥 FIX CHÍNH Ở ĐÂY

    // UI dùng → hiển thị toàn bộ team
    public List<RescueTeam> getAllTeams() {
        return rescueTeamRepository.findAll();
    }

    // Logic dùng → chỉ team available
    public List<RescueTeam> getAvailableTeams() {
        return rescueTeamRepository.findAll().stream()
                .filter(team -> !assignmentRepository.existsByTeam_IdAndStatusIn(team.getId(), ACTIVE_STATUSES))
                .toList();
    }

    @Transactional
    public Assignment revokeTask(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy assignment"));

        if (!ACTIVE_STATUSES.contains(assignment.getStatus())) {
            throw new BadRequestException("Chỉ nhiệm vụ đang thực thi hoặc báo Sự cố mới có thể thu hồi");
        }

        assignment.setStatus(STATUS_REVOKED);
        assignment.getRequest().setStatus("verified");
        assignment.getVehicle().setStatus(VEHICLE_AVAILABLE);

        vehicleRepository.save(assignment.getVehicle());
        requestService.save(assignment.getRequest());
        return assignmentRepository.save(assignment);
    }
}