package io.github.carlosolivserafim.order.api.service;

import io.github.carlosolivserafim.order.api.controller.BadRequestException;
import io.github.carlosolivserafim.order.api.controller.NotFoundException;
import io.github.carlosolivserafim.order.api.dto.OrderRecordDto;
import io.github.carlosolivserafim.order.api.dto.OrderResponseDto;
import io.github.carlosolivserafim.order.api.dto.UserApiResponse;
import io.github.carlosolivserafim.order.api.model.Order;
import io.github.carlosolivserafim.order.api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateOrderThrowsBadRequestException() {
        OrderRecordDto orderDto = new OrderRecordDto(1L, "Samsung S21", 2, 50.0);

        when(orderRepository.save(any(Order.class)))
                .thenThrow(new DataIntegrityViolationException("Constraint violation"));

        assertThrows(BadRequestException.class, () -> orderService.createOrder(orderDto));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindOrderByIdThrowsNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.findOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteOrderThrowsNotFoundException() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.deleteOrder(1L));
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindUserByIdSuccess() {
        UserApiResponse userApiResponse = new UserApiResponse(1L, "joao");
        when(restTemplate.getForEntity(anyString(), eq(UserApiResponse.class)))
                .thenReturn(new org.springframework.http.ResponseEntity<>(userApiResponse, HttpStatus.OK));

        UserApiResponse result = orderService.findUserById(1L);

        assertNotNull(result);
        assertEquals("joao", result.name());
    }
}