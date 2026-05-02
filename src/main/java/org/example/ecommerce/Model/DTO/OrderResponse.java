package org.example.ecommerce.Model.DTO;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String customerName,
        String orderId,
        String email,
        String status,
        LocalDate orderDate,
        List<OrderItemResponse> item
) {
}
