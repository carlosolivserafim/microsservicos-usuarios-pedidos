package io.github.carlosolivserafim.order.api.repository;

import io.github.carlosolivserafim.order.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}