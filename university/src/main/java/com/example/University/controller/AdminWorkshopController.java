package com.example.University.controller;

import com.example.University.dto.RegistrationResponseDTO;
import com.example.University.dto.WorkshopRequestDTO;
import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.service.RegistrationService;
import com.example.University.service.WorkshopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/workshops")
public class AdminWorkshopController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;

    public AdminWorkshopController(WorkshopService workshopService, RegistrationService registrationService) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllWorkshops());
    }

    @PostMapping
    public ResponseEntity<WorkshopResponseDTO> createWorkshop(@Valid @RequestBody WorkshopRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workshopService.createWorkshop(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshop(@PathVariable Long id,
                                                               @Valid @RequestBody WorkshopRequestDTO dto) {
        return ResponseEntity.ok(workshopService.updateWorkshop(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<WorkshopResponseDTO> cancelWorkshop(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.cancelWorkshop(id));
    }

    @GetMapping("/{id}/registrations")
    public ResponseEntity<List<RegistrationResponseDTO>> getWorkshopRegistrations(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationsForWorkshop(id));
    }
}
