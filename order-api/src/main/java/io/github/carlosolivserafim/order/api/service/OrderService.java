package io.github.carlosolivserafim.order.api.service;

import io.github.carlosolivserafim.order.api.controller.BadRequestException;
import io.github.carlosolivserafim.order.api.controller.NotFoundException;
import io.github.carlosolivserafim.order.api.dto.OrderRecordDto;
import io.github.carlosolivserafim.order.api.dto.OrderResponseDto;
import io.github.carlosolivserafim.order.api.dto.UserApiResponse;
import io.github.carlosolivserafim.order.api.model.Order;
import io.github.carlosolivserafim.order.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponseDto createOrder(OrderRecordDto orderDto){
        Double valorTotalCalculado = orderDto.itemQuantity() * orderDto.itemPrice();
        UserApiResponse userApiResponse = findUserById(orderDto.userId());

        Order order = new Order();
        order.setUserId(orderDto.userId());
        order.setItemDescription(orderDto.itemDescription());
        order.setItemQuantity(orderDto.itemQuantity());
        order.setItemPrice(orderDto.itemPrice());
        order.setTotalValue(valorTotalCalculado);
        order.setCreatedAt(LocalDateTime.now());

        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException ex){
            throw new BadRequestException(ex.getMessage());
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto(order.getId(),
                userApiResponse,
                order.getItemDescription(),
                order.getItemQuantity(),
                order.getItemPrice(),
                order.getTotalValue(),
                order.getCreatedAt(),
                order.getUpdatedAt());

        return orderResponseDto;
    }

    @Cacheable(value = "orders")
    public List<Order> findAllOrdens(){
        return orderRepository.findAll();
    }

    public OrderResponseDto findOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado com o ID: "+id));

        UserApiResponse userApiResponse = findUserById(order.getUserId());

        OrderResponseDto orderResponseDto = new OrderResponseDto(order.getId(),
                userApiResponse,
                order.getItemDescription(),
                order.getItemQuantity(),
                order.getItemPrice(),
                order.getTotalValue(),
                order.getCreatedAt(),
                order.getUpdatedAt());

        return orderResponseDto;
    }

    @CacheEvict(value = "orders", allEntries = true)
    public Order updateOrder(Long id, OrderRecordDto orderDto) {
        OrderResponseDto existingOrder = findOrderById(id);
        Double valueTotalCalculated = orderDto.itemQuantity() * orderDto.itemPrice();

        Order order = new Order();
        order.setId(existingOrder.Id());
        order.setCreatedAt(existingOrder.createdAt());
        order.setUserId(existingOrder.user().id());
        order.setItemDescription(orderDto.itemDescription());
        order.setItemPrice(orderDto.itemPrice());
        order.setItemQuantity(orderDto.itemQuantity());
        order.setTotalValue(valueTotalCalculated);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(Long id) {
        if(existsOrderById(id)){
            orderRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Ordem com o ID %s não encontrada.", id));
        }
    }

    public boolean existsOrderById(Long id){
        return orderRepository.existsById(id);
    }

    public UserApiResponse findUserById(Long userId){
        String userUrl = String.format("http://localhost:8080/users/%s",userId);
        UserApiResponse userApiResponse = null;

        RestTemplate client = new RestTemplate();
        try {
            userApiResponse = client.getForEntity(userUrl, UserApiResponse.class).getBody();
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException("Usuário não encontrado para o ID: "+userId);
            }
        }
        return userApiResponse;
    }
}