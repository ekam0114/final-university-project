package com.example.University.integration;

import com.example.University.model.Role;
import com.example.University.model.User;
import com.example.University.model.Workshop;
import com.example.University.repository.UserRepository;
import com.example.University.repository.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkshopIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired WorkshopRepository workshopRepository;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        workshopRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void publicEndpointReturnsWorkshopList() throws Exception {
        Workshop w = new Workshop();
        w.setTitle("Spring Boot Basics");
        w.setLocation("Room 101");
        w.setDescription("Learn Spring");
        w.setStartDatetime(LocalDateTime.now().plusDays(5));
        w.setTotalSeats(20);
        w.setSeatsRemaining(20);
        w.setStatus("ACTIVE");
        workshopRepository.save(w);

        mockMvc.perform(get("/api/v1/workshops").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Boot Basics"));
    }

    @Test
    void adminCanCreateWorkshop() throws Exception {
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        String payload = String.format(
            "{\"title\":\"Docker Workshop\",\"description\":\"Learn Docker\",\"location\":\"Lab B\",\"startDatetime\":\"%s\",\"totalSeats\":15}",
            LocalDateTime.now().plusDays(3)
        );

        mockMvc.perform(post("/api/v1/admin/workshops")
                .with(user("admin@test.com").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Docker Workshop"));
    }

    @Test
    void unauthenticatedUserCannotRegister() throws Exception {
        Workshop w = new Workshop();
        w.setTitle("Python Workshop");
        w.setLocation("Hall C");
        w.setDescription("Python basics");
        w.setStartDatetime(LocalDateTime.now().plusDays(2));
        w.setTotalSeats(10);
        w.setSeatsRemaining(10);
        w.setStatus("ACTIVE");
        workshopRepository.save(w);

        mockMvc.perform(post("/api/v1/workshops/" + w.getId() + "/registrations"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void workshopValidationRejectsShortTitle() throws Exception {
        String payload = String.format(
            "{\"title\":\"AB\",\"location\":\"Room 101\",\"startDatetime\":\"%s\",\"totalSeats\":10}",
            LocalDateTime.now().plusDays(1)
        );

        mockMvc.perform(post("/api/v1/admin/workshops")
                .with(user("admin@test.com").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}
