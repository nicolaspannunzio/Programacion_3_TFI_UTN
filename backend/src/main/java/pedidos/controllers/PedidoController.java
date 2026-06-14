package pedidos.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.pedido.PedidoCreate;
import pedidos.dtos.pedido.PedidoEdit;
import pedidos.entidades.Pedido;
import pedidos.services.PedidoService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody PedidoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Pedido>> buscarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody PedidoEdit dto) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}