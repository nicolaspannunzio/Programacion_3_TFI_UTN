package pedidos.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.producto.ProductoCreate;
import pedidos.dtos.producto.ProductoEdit;
import pedidos.entidades.Producto;
import pedidos.services.ProductoService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody ProductoEdit dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}