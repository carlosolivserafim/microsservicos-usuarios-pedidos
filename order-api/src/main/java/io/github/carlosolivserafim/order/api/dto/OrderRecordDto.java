package io.github.carlosolivserafim.order.api.dto;

public record OrderRecordDto(Long userId,
                             String itemDescription,
                             Integer itemQuantity,
                             Double itemPrice) {
}