package es.unir.dwfs.payments.controller;

import es.unir.dwfs.payments.controller.model.OrderRequest;
import es.unir.dwfs.payments.data.model.Order;
import es.unir.dwfs.payments.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Controlador REST para gestión de pedidos
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders Controller", description = "API para gestión de pedidos de libros")
public class OrdersController {

    private final OrdersService service;

    @PostMapping("/orders")
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido validando que los libros existan y estén visibles", responses = {
            @ApiResponse(responseCode = "200", description = "Pedido creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación (libro no existe o no está visible)")
    })
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequest request) {

        log.info("Creating order with request: {}", request);

        // El servicio lanza ResponseStatusException si hay error de validación
        Order created = service.createOrder(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/orders")
    @Operation(summary = "Listar pedidos", description = "Obtiene todos los pedidos registrados", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    })
    public ResponseEntity<List<Order>> getOrders() {

        List<Order> orders = service.getOrders();
        if (orders != null) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido específico por su identificador", responses = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Order> getOrder(@PathVariable String id) {

        Order order = service.getOrder(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
