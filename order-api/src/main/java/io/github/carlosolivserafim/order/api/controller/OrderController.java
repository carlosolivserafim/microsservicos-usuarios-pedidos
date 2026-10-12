package io.github.carlosolivserafim.order.api.controller;

import io.github.carlosolivserafim.order.api.dto.OrderRecordDto;
import io.github.carlosolivserafim.order.api.dto.OrderResponseDto;
import io.github.carlosolivserafim.order.api.model.Order;
import io.github.carlosolivserafim.order.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> insertOrder(@RequestBody OrderRecordDto orderRecordDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAllOrdens(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOrderById(@PathVariable(name = "id") long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable(name = "id") long id, @RequestBody OrderRecordDto orderRecordDto){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(id, orderRecordDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}