package com.example.University.dto;

import java.time.LocalDateTime;

public class WorkshopResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDatetime;
    private int totalSeats;
    private int seatsRemaining;
    private String status;

    public WorkshopResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(LocalDateTime startDatetime) { this.startDatetime = startDatetime; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getSeatsRemaining() { return seatsRemaining; }
    public void setSeatsRemaining(int seatsRemaining) { this.seatsRemaining = seatsRemaining; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
