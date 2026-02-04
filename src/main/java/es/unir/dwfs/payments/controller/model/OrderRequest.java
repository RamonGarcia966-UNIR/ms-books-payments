package es.unir.dwfs.payments.controller.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "ORDER-001")
    @Size(min = 1, message = "ORDER-002")
    @Valid
    private List<OrderItemRequest> items;
}
