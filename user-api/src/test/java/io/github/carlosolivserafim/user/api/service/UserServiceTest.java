package io.github.carlosolivserafim.user.api.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.github.carlosolivserafim.user.api.controller.BadRequestException;
import io.github.carlosolivserafim.user.api.controller.NotFoundException;
import io.github.carlosolivserafim.user.api.dto.UserRecordDto;
import io.github.carlosolivserafim.user.api.model.User;
import io.github.carlosolivserafim.user.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserThrowsBadRequestException() {
        UserRecordDto userDto = new UserRecordDto("John Doe", "12345678910", "john@example.com", "123456789");

        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        assertThrows(BadRequestException.class, () -> userService.createUser(userDto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() {
        UserRecordDto userDto = new UserRecordDto("Jane Doe", "98765432100", "jane@example.com", "987654321");
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, userDto);

        assertNotNull(updatedUser);
        assertEquals(userDto.name(), updatedUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserThrowsNotFoundException() {
        UserRecordDto userDto = new UserRecordDto("Jane Doe", "98765432100", "jane@example.com", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, userDto));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUserThrowsNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
    }
}
