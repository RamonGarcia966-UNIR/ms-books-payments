package es.unir.dwfs.payments.controller.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Request para crear un pedido
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderRequest {

    @NotEmpty(message = "El pedido debe contener al menos un item")
    @Valid
    private List<OrderItemRequest> items;
}
