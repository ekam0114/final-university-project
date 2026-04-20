package com.example.University.dto;

import java.time.LocalDateTime;

public class RegistrationResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long workshopId;
    private String workshopTitle;
    private LocalDateTime workshopStartDatetime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;

    public RegistrationResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Long getWorkshopId() { return workshopId; }
    public void setWorkshopId(Long workshopId) { this.workshopId = workshopId; }
    public String getWorkshopTitle() { return workshopTitle; }
    public void setWorkshopTitle(String workshopTitle) { this.workshopTitle = workshopTitle; }
    public LocalDateTime getWorkshopStartDatetime() { return workshopStartDatetime; }
    public void setWorkshopStartDatetime(LocalDateTime workshopStartDatetime) { this.workshopStartDatetime = workshopStartDatetime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
}
