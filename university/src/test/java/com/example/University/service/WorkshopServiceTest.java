package com.example.University.service;

import com.example.University.dto.WorkshopRequestDTO;
import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.exception.ResourceNotFoundException;
import com.example.University.mapper.WorkshopMapper;
import com.example.University.model.Workshop;
import com.example.University.repository.WorkshopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkshopServiceTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private WorkshopMapper workshopMapper;

    @InjectMocks
    private WorkshopService workshopService;

    @Test
    void createWorkshopSuccessfully() {
        WorkshopRequestDTO dto = new WorkshopRequestDTO();
        dto.setTitle("Spring Boot Workshop");
        dto.setDescription("Learn Spring Boot");
        dto.setLocation("Room 101");
        dto.setStartDatetime(LocalDateTime.now().plusDays(2));
        dto.setTotalSeats(30);

        Workshop savedWorkshop = new Workshop();
        savedWorkshop.setId(1L);
        savedWorkshop.setTitle(dto.getTitle());
        savedWorkshop.setSeatsRemaining(30);
        savedWorkshop.setStatus("ACTIVE");

        WorkshopResponseDTO responseDTO = new WorkshopResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Spring Boot Workshop");
        responseDTO.setTotalSeats(30);
        responseDTO.setSeatsRemaining(30);
        responseDTO.setStatus("ACTIVE");

        when(workshopRepository.save(any(Workshop.class))).thenReturn(savedWorkshop);
        when(workshopMapper.toResponseDTO(savedWorkshop)).thenReturn(responseDTO);

        WorkshopResponseDTO result = workshopService.createWorkshop(dto);

        assertNotNull(result);
        assertEquals("Spring Boot Workshop", result.getTitle());
        assertEquals(30, result.getTotalSeats());
        assertEquals("ACTIVE", result.getStatus());

        verify(workshopRepository).save(any(Workshop.class));
        verify(workshopMapper).toResponseDTO(savedWorkshop);
    }

    @Test
    void cancelWorkshopSuccessfully() {
        Workshop workshop = new Workshop();
        workshop.setId(1L);
        workshop.setTitle("Java Basics");
        workshop.setStatus("ACTIVE");

        Workshop cancelled = new Workshop();
        cancelled.setId(1L);
        cancelled.setStatus("CANCELLED");

        WorkshopResponseDTO responseDTO = new WorkshopResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStatus("CANCELLED");

        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(workshopRepository.save(workshop)).thenReturn(cancelled);
        when(workshopMapper.toResponseDTO(cancelled)).thenReturn(responseDTO);

        WorkshopResponseDTO result = workshopService.cancelWorkshop(1L);

        assertEquals("CANCELLED", result.getStatus());
        verify(workshopRepository).save(workshop);
    }

    @Test
    void getWorkshopByIdThrowsWhenNotFound() {
        when(workshopRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.getWorkshopById(99L));
    }
}
