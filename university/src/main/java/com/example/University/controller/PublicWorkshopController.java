package com.example.University.controller;

import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.service.WorkshopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workshops")
public class PublicWorkshopController {

    private final WorkshopService workshopService;

    public PublicWorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllActiveUpcomingWorkshops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopResponseDTO> getWorkshopById(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.getWorkshopById(id));
    }
}
