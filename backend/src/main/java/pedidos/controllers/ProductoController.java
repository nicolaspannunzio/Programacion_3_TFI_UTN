package pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.producto.ProductoCreate;
import pedidos.dtos.producto.ProductoEdit;
import pedidos.entidades.Producto;
import pedidos.services.ProductoService;

@Tag(name = "Productos", description = "Gestión del catálogo de productos")
@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Listar todos los productos activos")
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(summary = "Listar productos por categoría")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(id));
    }

    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @Operation(summary = "Actualizar un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody ProductoEdit dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un producto (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}