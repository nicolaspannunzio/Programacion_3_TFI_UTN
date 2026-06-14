package pedidos.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.categoria.CategoriaCreate;
import pedidos.dtos.categoria.CategoriaEdit;
import pedidos.entidades.Categoria;
import pedidos.services.CategoriaService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody CategoriaCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody CategoriaEdit dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}