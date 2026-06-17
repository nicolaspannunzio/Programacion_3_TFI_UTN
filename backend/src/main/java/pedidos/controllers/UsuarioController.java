package pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.usuario.UsuarioCreate;
import pedidos.dtos.usuario.UsuarioEdit;
import pedidos.entidades.Usuario;
import pedidos.services.UsuarioService;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios activos")
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "Actualizar un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody UsuarioEdit dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un usuario (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Iniciar sesión — usar /api/auth/login en su lugar")
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> body) {
        String mail = body.get("mail");
        String contraseña = body.get("contraseña");
        return ResponseEntity.ok(usuarioService.login(mail, contraseña));
    }
}