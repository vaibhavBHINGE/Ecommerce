package org.example.ecommerce.Model.DTO;

public record OrderItemRequest(
        int productId,
        int quantity

) {
}
