package com.example.University.service;

import com.example.University.dto.WorkshopRequestDTO;
import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.exception.ResourceNotFoundException;
import com.example.University.mapper.WorkshopMapper;
import com.example.University.model.Workshop;
import com.example.University.repository.WorkshopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    public WorkshopService(WorkshopRepository workshopRepository, WorkshopMapper workshopMapper) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
    }

    @Transactional
    public List<WorkshopResponseDTO> getAllActiveUpcomingWorkshops() {
        return workshopRepository
                .findByStatusAndStartDatetimeAfter("ACTIVE", LocalDateTime.now())
                .stream()
                .map(workshopMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkshopResponseDTO getWorkshopById(Long id) {
        return workshopMapper.toResponseDTO(findWorkshopOrThrow(id));
    }

    @Transactional
    public List<WorkshopResponseDTO> getAllWorkshops() {
        return workshopRepository.findAll()
                .stream()
                .map(workshopMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkshopResponseDTO createWorkshop(WorkshopRequestDTO dto) {
        Workshop workshop = new Workshop();
        workshop.setTitle(dto.getTitle());
        workshop.setDescription(dto.getDescription());
        workshop.setLocation(dto.getLocation());
        workshop.setStartDatetime(dto.getStartDatetime());
        workshop.setTotalSeats(dto.getTotalSeats());
        workshop.setSeatsRemaining(dto.getTotalSeats());
        workshop.setStatus("ACTIVE");
        return workshopMapper.toResponseDTO(workshopRepository.save(workshop));
    }

    @Transactional
    public WorkshopResponseDTO updateWorkshop(Long id, WorkshopRequestDTO dto) {
        Workshop workshop = findWorkshopOrThrow(id);
        workshop.setTitle(dto.getTitle());
        workshop.setDescription(dto.getDescription());
        workshop.setLocation(dto.getLocation());
        workshop.setStartDatetime(dto.getStartDatetime());
        int diff = dto.getTotalSeats() - workshop.getTotalSeats();
        workshop.setTotalSeats(dto.getTotalSeats());
        workshop.setSeatsRemaining(Math.max(0, workshop.getSeatsRemaining() + diff));
        return workshopMapper.toResponseDTO(workshopRepository.save(workshop));
    }

    @Transactional
    public WorkshopResponseDTO cancelWorkshop(Long id) {
        Workshop workshop = findWorkshopOrThrow(id);
        workshop.setStatus("CANCELLED");
        return workshopMapper.toResponseDTO(workshopRepository.save(workshop));
    }

    public Workshop findWorkshopOrThrow(Long id) {
        return workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found with id: " + id));
    }
}
