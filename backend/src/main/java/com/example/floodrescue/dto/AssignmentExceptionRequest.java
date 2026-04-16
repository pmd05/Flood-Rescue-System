package com.example.floodrescue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AssignmentExceptionRequest {
    
    @NotBlank(message = "Trạng thái báo cáo không được để trống")
    @Pattern(regexp = "^(blocked|need_backup)$", message = "Trạng thái báo cáo không hợp lệ")
    private String status;
    
    @NotBlank(message = "Chi tiết báo cáo không được để trống")
    private String note;
    
    private String image;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
