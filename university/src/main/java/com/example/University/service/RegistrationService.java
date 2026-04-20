package com.example.University.service;

import com.example.University.dto.RegistrationResponseDTO;
import com.example.University.exception.*;
import com.example.University.mapper.RegistrationMapper;
import com.example.University.model.Registration;
import com.example.University.model.User;
import com.example.University.model.Workshop;
import com.example.University.repository.RegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final WorkshopService workshopService;
    private final UserService userService;
    private final RegistrationMapper registrationMapper;

    public RegistrationService(RegistrationRepository registrationRepository,
                               WorkshopService workshopService,
                               UserService userService,
                               RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.workshopService = workshopService;
        this.userService = userService;
        this.registrationMapper = registrationMapper;
    }

    @Transactional
    public RegistrationResponseDTO registerUserForWorkshop(Long workshopId, String userEmail) {
        User user = userService.findByEmail(userEmail);
        Workshop workshop = workshopService.findWorkshopOrThrow(workshopId);

        if ("CANCELLED".equals(workshop.getStatus())) {
            throw new WorkshopPastException("Cannot register for a cancelled workshop.");
        }

        // Rule C — Past workshop
        if (workshop.getStartDatetime().isBefore(LocalDateTime.now())) {
            throw new WorkshopPastException("Cannot register for a workshop that has already started or passed.");
        }

        // Rule B — No duplicate registration
        if (registrationRepository.existsByUserIdAndWorkshopId(user.getId(), workshopId)) {
            throw new DuplicateRegistrationException("You are already registered for this workshop.");
        }

        // Rule A — Seat limit
        if (workshop.getSeatsRemaining() <= 0) {
            throw new SeatLimitException("This workshop is fully booked. No seats remaining.");
        }

        workshop.setSeatsRemaining(workshop.getSeatsRemaining() - 1);

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setWorkshop(workshop);
        registration.setStatus("ACTIVE");

        return registrationMapper.toResponseDTO(registrationRepository.save(registration));
    }

    @Transactional
    public RegistrationResponseDTO cancelRegistration(Long registrationId, String userEmail, boolean isAdmin) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        if (!isAdmin && !registration.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You are not authorized to cancel this registration.");
        }

        if ("CANCELLED".equals(registration.getStatus())) {
            throw new WorkshopPastException("This registration is already cancelled.");
        }

        // Rule D — Cannot cancel after workshop starts
        Workshop workshop = registration.getWorkshop();
        if (workshop.getStartDatetime().isBefore(LocalDateTime.now())) {
            throw new WorkshopPastException("Cannot cancel a registration after the workshop has started.");
        }

        registration.setStatus("CANCELLED");
        registration.setCancelledAt(LocalDateTime.now());
        workshop.setSeatsRemaining(workshop.getSeatsRemaining() + 1);

        return registrationMapper.toResponseDTO(registrationRepository.save(registration));
    }

    @Transactional
    public List<RegistrationResponseDTO> getMyRegistrations(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return registrationRepository.findByUserId(user.getId())
                .stream()
                .map(registrationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RegistrationResponseDTO> getRegistrationsForWorkshop(Long workshopId) {
        workshopService.findWorkshopOrThrow(workshopId);
        return registrationRepository.findByWorkshopId(workshopId)
                .stream()
                .map(registrationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
