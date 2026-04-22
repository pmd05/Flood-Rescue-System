package com.example.floodrescue.service;

import com.example.floodrescue.dto.VehicleRequest;
import com.example.floodrescue.exception.BadRequestException;
import com.example.floodrescue.exception.ResourceNotFoundException;
import com.example.floodrescue.model.Vehicle;
import com.example.floodrescue.repository.AssignmentRepository;
import com.example.floodrescue.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final AssignmentRepository assignmentRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          AssignmentRepository assignmentRepository) {
        this.vehicleRepository = vehicleRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public Vehicle create(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setName(request.getName().trim());
        vehicle.setStatus(normalizeStatus(request.getStatus()));
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle update(Long id, VehicleRequest request) {
        Vehicle vehicle = getById(id);
        vehicle.setName(request.getName().trim());
        vehicle.setStatus(normalizeStatus(request.getStatus()));
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {
        if (assignmentRepository.existsByVehicle_Id(id)) {
            throw new BadRequestException("Không thể xóa vehicle đã có lịch sử phân công");
        }
        vehicleRepository.delete(getById(id));
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vehicle với id = " + id));
    }

    private String normalizeStatus(String status) {
        String value = status == null ? "available" : status.trim().toLowerCase();
        if (!List.of("available", "in_use", "maintenance").contains(value)) {
            throw new BadRequestException("Vehicle status không hợp lệ");
        }
        return value;
    }
}
