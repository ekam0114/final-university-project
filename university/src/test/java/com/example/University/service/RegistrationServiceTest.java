package com.example.University.service;

import com.example.University.dto.RegistrationResponseDTO;
import com.example.University.exception.DuplicateRegistrationException;
import com.example.University.exception.SeatLimitException;
import com.example.University.exception.WorkshopPastException;
import com.example.University.mapper.RegistrationMapper;
import com.example.University.model.Registration;
import com.example.University.model.Role;
import com.example.University.model.User;
import com.example.University.model.Workshop;
import com.example.University.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock private RegistrationRepository registrationRepository;
    @Mock private WorkshopService workshopService;
    @Mock private UserService userService;
    @Mock private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegistrationService registrationService;

    private User user;
    private Workshop workshop;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@test.com");
        user.setRole(Role.ATTENDEE);

        workshop = new Workshop();
        workshop.setId(1L);
        workshop.setTitle("Java Workshop");
        workshop.setLocation("Lab A");
        workshop.setStartDatetime(LocalDateTime.now().plusDays(1));
        workshop.setTotalSeats(10);
        workshop.setSeatsRemaining(5);
        workshop.setStatus("ACTIVE");
    }

    @Test
    void registerUserSuccessfully() {
        Registration savedReg = new Registration();
        savedReg.setId(1L);
        savedReg.setUser(user);
        savedReg.setWorkshop(workshop);
        savedReg.setStatus("ACTIVE");

        RegistrationResponseDTO dto = new RegistrationResponseDTO();
        dto.setId(1L);
        dto.setStatus("ACTIVE");

        when(userService.findByEmail("alice@test.com")).thenReturn(user);
        when(workshopService.findWorkshopOrThrow(1L)).thenReturn(workshop);
        when(registrationRepository.existsByUserIdAndWorkshopId(1L, 1L)).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(savedReg);
        when(registrationMapper.toResponseDTO(savedReg)).thenReturn(dto);

        RegistrationResponseDTO result = registrationService.registerUserForWorkshop(1L, "alice@test.com");

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(4, workshop.getSeatsRemaining());
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void shouldThrowWhenAlreadyRegistered() {
        when(userService.findByEmail("alice@test.com")).thenReturn(user);
        when(workshopService.findWorkshopOrThrow(1L)).thenReturn(workshop);
        when(registrationRepository.existsByUserIdAndWorkshopId(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateRegistrationException.class,
                () -> registrationService.registerUserForWorkshop(1L, "alice@test.com"));

        verify(registrationRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenWorkshopFull() {
        workshop.setSeatsRemaining(0);

        when(userService.findByEmail("alice@test.com")).thenReturn(user);
        when(workshopService.findWorkshopOrThrow(1L)).thenReturn(workshop);
        when(registrationRepository.existsByUserIdAndWorkshopId(1L, 1L)).thenReturn(false);

        assertThrows(SeatLimitException.class,
                () -> registrationService.registerUserForWorkshop(1L, "alice@test.com"));

        verify(registrationRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenWorkshopInPast() {
        workshop.setStartDatetime(LocalDateTime.now().minusDays(1));

        when(userService.findByEmail("alice@test.com")).thenReturn(user);
        when(workshopService.findWorkshopOrThrow(1L)).thenReturn(workshop);

        assertThrows(WorkshopPastException.class,
                () -> registrationService.registerUserForWorkshop(1L, "alice@test.com"));
    }

    @Test
    void shouldThrowWhenWorkshopCancelled() {
        workshop.setStatus("CANCELLED");

        when(userService.findByEmail("alice@test.com")).thenReturn(user);
        when(workshopService.findWorkshopOrThrow(1L)).thenReturn(workshop);

        assertThrows(WorkshopPastException.class,
                () -> registrationService.registerUserForWorkshop(1L, "alice@test.com"));
    }
}
