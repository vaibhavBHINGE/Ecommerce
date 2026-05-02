package org.example.ecommerce.Model.DTO;

import java.util.List;

public record OrderRequest(
        String customerName,
        String email,
        List<OrderItemRequest>item
) {
}
