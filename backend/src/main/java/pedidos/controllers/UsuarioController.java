package pedidos.controllers;

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

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody UsuarioEdit dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> body) {
        String mail = body.get("mail");
        String contraseña = body.get("contraseña");
        return ResponseEntity.ok(usuarioService.login(mail, contraseña));
    }
}