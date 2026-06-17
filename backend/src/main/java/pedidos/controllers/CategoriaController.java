package pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.categoria.CategoriaCreate;
import pedidos.dtos.categoria.CategoriaEdit;
import pedidos.entidades.Categoria;
import pedidos.services.CategoriaService;

@Tag(name = "Categorías", description = "Gestión de categorías de productos")
@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Listar todas las categorías activas")
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(summary = "Obtener una categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Operation(summary = "Crear una nueva categoría")
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody CategoriaCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(dto));
    }

    @Operation(summary = "Actualizar una categoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody CategoriaEdit dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar una categoría (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}