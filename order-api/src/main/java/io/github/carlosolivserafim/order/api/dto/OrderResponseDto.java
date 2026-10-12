package io.github.carlosolivserafim.order.api.dto;

import java.time.LocalDateTime;

public record OrderResponseDto(Long Id,
                               UserApiResponse user,
                               String itemDescription,
                               Integer itemQuantity,
                               Double itemPrice,
                               Double totalValue,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
}