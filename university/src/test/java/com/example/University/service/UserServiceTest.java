package com.example.University.service;

import com.example.University.dto.UserRegistrationRequestDTO;
import com.example.University.exception.EmailAlreadyExistsException;
import com.example.University.model.Role;
import com.example.University.model.User;
import com.example.University.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserSuccessfully() {
        UserRegistrationRequestDTO dto = new UserRegistrationRequestDTO();
        dto.setName("Alice");
        dto.setEmail("alice@test.com");
        dto.setPassword("password123");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed_password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(dto.getName());
        savedUser.setEmail(dto.getEmail());
        savedUser.setPassword("hashed_password");
        savedUser.setRole(Role.ATTENDEE);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("alice@test.com", result.getEmail());
        assertEquals(Role.ATTENDEE, result.getRole());

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        UserRegistrationRequestDTO dto = new UserRegistrationRequestDTO();
        dto.setName("Alice");
        dto.setEmail("alice@test.com");
        dto.setPassword("password123");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(dto));

        verify(userRepository, never()).save(any(User.class));
    }
}
