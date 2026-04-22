package com.example.floodrescue.service;

import com.example.floodrescue.dto.ReliefItemRequest;
import com.example.floodrescue.exception.BadRequestException;
import com.example.floodrescue.exception.ResourceNotFoundException;
import com.example.floodrescue.model.ReliefItem;
import com.example.floodrescue.repository.InventoryLogRepository;
import com.example.floodrescue.repository.ReliefItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReliefItemService {
    private final ReliefItemRepository reliefItemRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public ReliefItemService(ReliefItemRepository reliefItemRepository,
                             InventoryLogRepository inventoryLogRepository) {
        this.reliefItemRepository = reliefItemRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }

    public ReliefItem create(ReliefItemRequest request) {
        ReliefItem item = new ReliefItem();
        item.setName(request.getName().trim());
        item.setQuantity(validateQuantity(request.getQuantity()));
        return reliefItemRepository.save(item);
    }

    public List<ReliefItem> getAll() {
        return reliefItemRepository.findAll();
    }

    public ReliefItem update(Long id, ReliefItemRequest request) {
        ReliefItem item = getById(id);
        item.setName(request.getName().trim());
        item.setQuantity(validateQuantity(request.getQuantity()));
        return reliefItemRepository.save(item);
    }

    public void delete(Long id) {
        if (inventoryLogRepository.existsByItem_Id(id)) {
            throw new BadRequestException("Không thể xóa vật tư đã có lịch sử kho");
        }
        reliefItemRepository.delete(getById(id));
    }

    public ReliefItem getById(Long id) {
        return reliefItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy relief item với id = " + id));
    }

    private int validateQuantity(Integer quantity) {
        int value = quantity == null ? 0 : quantity;
        if (value < 0) {
            throw new BadRequestException("Số lượng vật tư không được âm");
        }
        return value;
    }
}
