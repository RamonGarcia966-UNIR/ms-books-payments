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
@Tag(name = "Orders Controller", description = "API REST para gestión de pedidos de libros. Incluye validación de existencia y visibilidad de libros con manejo robusto de errores")
public class OrdersController {

    private final OrdersService service;

    @PostMapping("/orders")
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido validando que todos los libros existan en el catálogo y estén visibles para la venta. Captura el precio actual de cada libro en el momento del pedido", responses = {
            @ApiResponse(responseCode = "200", description = "OK - Pedido creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Error de validación (libro no existe, libro no está visible, o datos inválidos)"),
            @ApiResponse(responseCode = "409", description = "Conflict - Violación de restricción de integridad de datos"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado del servidor")
    })
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequest request) {

        log.info("Creating order with request: {}", request);

        // El servicio lanza ResponseStatusException si hay error de validación
        Order created = service.createOrder(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/orders")
    @Operation(summary = "Listar pedidos", description = "Obtiene todos los pedidos registrados en el sistema", responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista de pedidos devuelta exitosamente"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado del servidor")
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
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido específico del sistema mediante su identificador único", responses = {
            @ApiResponse(responseCode = "200", description = "OK - Pedido encontrado y devuelto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Not Found - No existe un pedido con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado del servidor")
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
