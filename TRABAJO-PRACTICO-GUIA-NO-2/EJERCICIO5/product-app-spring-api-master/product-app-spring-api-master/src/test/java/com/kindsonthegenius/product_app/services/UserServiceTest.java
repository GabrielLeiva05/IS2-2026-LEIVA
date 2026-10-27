package com.kindsonthegenius.product_app.services;

import com.kindsonthegenius.product_app.model.User;
import com.kindsonthegenius.product_app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository, new BCryptPasswordEncoder());
    }

    @Test
    void authenticatesWithTheRegisteredPassword() {
        User user = new User();
        user.setUsername("gabri");
        user.setPassword(new BCryptPasswordEncoder().encode("clave123"));
        when(userRepository.findByUsername("gabri")).thenReturn(user);

        assertTrue(userService.authenticate("gabri", "clave123"));
    }

    @Test
    void rejectsWrongPasswordAndUnknownUser() {
        User user = new User();
        user.setUsername("gabri");
        user.setPassword(new BCryptPasswordEncoder().encode("clave123"));
        when(userRepository.findByUsername("gabri")).thenReturn(user);
        when(userRepository.findByUsername("desconocido")).thenReturn(null);

        assertFalse(userService.authenticate("gabri", "incorrecta"));
        assertFalse(userService.authenticate("desconocido", "clave123"));
    }

    @Test
    void authenticatesSeedAdminCredentials() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("$2a$10$NTdmA1A4kqyYw1AbKhqKyOIlP63RhyP4iIshQIIdbFPfqRYqRjCpS");
        when(userRepository.findByUsername("admin")).thenReturn(user);

        assertTrue(userService.authenticate("admin", "password"));
    }
}
