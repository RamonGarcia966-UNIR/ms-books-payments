package es.unir.dwfs.payments.controller.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request para un item del pedido
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemRequest {

    @NotNull(message = "ORDER_ITEM-001")
    @Min(value = 1, message = "ORDER_ITEM-002")
    private Long bookId;

    @NotNull(message = "ORDER_ITEM-010")
    @Min(value = 1, message = "ORDER_ITEM-011")
    @Max(value = 999, message = "ORDER_ITEM-012")
    private Integer quantity;
}
