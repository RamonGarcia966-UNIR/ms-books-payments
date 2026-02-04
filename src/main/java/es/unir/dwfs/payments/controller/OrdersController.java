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
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido validando que todos los libros existan en el catálogo y estén visibles para la venta", responses = {
            @ApiResponse(responseCode = "200", description = "OK - Pedido creado correctamente"),
            @ApiResponse(responseCode = "400", description = """
                    Bad Request - La petición contiene errores de formato, sintaxis o validación de datos que impiden su procesamiento. Los errores por validación de datos vienen detallados en el atributo 'details' de la respuesta y pueden ser:

                    **Campo 'items':**
                    - **ORDER-001**: El parámetro 'items' es obligatorio y no puede estar vacío
                    - **ORDER-002**: El pedido debe contener al menos un item

                    **Campo 'bookId' (del item):**
                    - **ORDER_ITEM-001**: El parámetro 'bookId' es obligatorio y no puede estar vacío
                    - **ORDER_ITEM-002**: El parámetro 'bookId' debe ser mayor a 0

                    **Campo 'quantity' (del item):**
                    - **ORDER_ITEM-010**: El parámetro 'quantity' es obligatorio y no puede estar vacío
                    - **ORDER_ITEM-011**: El parámetro 'quantity' debe ser al menos 1
                    - **ORDER_ITEM-012**: El parámetro 'quantity' no puede superar 999 unidades
                    """),
            @ApiResponse(responseCode = "422", description = """
                    Unprocessable Entity - La petición está bien formada pero contiene errores de lógica de negocio. Los errores vienen detallados en el atributo 'details' de la respuesta y pueden ser:

                    **Validación de libros:**
                    - **ORDER_BUSINESS-001**: El libro con ID {0} no existe en el catálogo
                    - **ORDER_BUSINESS-002**: El libro con ID {0} no está disponible para la venta
                    """),
            @ApiResponse(responseCode = "409", description = """
                    Conflict - Violación de restricción de integridad de datos. Los errores pueden ser:

                    **Errores genéricos:**
                    - **GENERIC-001**: Ya existe un registro con el mismo identificador
                    - **GENERIC-002**: Faltan campos obligatorios
                    - **GENERIC-003**: Error de integridad de datos
                    - **GENERIC-004**: El registro ya existe en el sistema
                    """),
            @ApiResponse(responseCode = "500", description = """
                    Internal Server Error - Error inesperado del servidor:

                    - **GENERIC-005**: Ha ocurrido un error inesperado. Por favor, contacte al administrador
                    """)
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
            @ApiResponse(responseCode = "500", description = """
                    Internal Server Error - Error inesperado del servidor:

                    - **GENERIC-005**: Ha ocurrido un error inesperado. Por favor, contacte al administrador
                    """)
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
            @ApiResponse(responseCode = "500", description = """
                    Internal Server Error - Error inesperado del servidor:

                    - **GENERIC-005**: Ha ocurrido un error inesperado. Por favor, contacte al administrador
                    """)
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
