package pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.pedido.PedidoCreate;
import pedidos.dtos.pedido.PedidoEdit;
import pedidos.entidades.Pedido;
import pedidos.services.PedidoService;

@Tag(name = "Pedidos", description = "Gestión de pedidos del sistema")
@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Listar todos los pedidos activos")
    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @Operation(summary = "Obtener un pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @Operation(summary = "Listar pedidos de un usuario específico")
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Pedido>> buscarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorUsuario(id));
    }

    @Operation(summary = "Crear un nuevo pedido")
    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody PedidoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(dto));
    }

    @Operation(summary = "Actualizar estado o forma de pago de un pedido")
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody PedidoEdit dto) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, dto));
    }

    @Operation(summary = "Eliminar un pedido (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}