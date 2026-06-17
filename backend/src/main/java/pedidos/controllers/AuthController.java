package pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.usuario.UsuarioCreate;
import pedidos.entidades.Usuario;
import pedidos.services.UsuarioService;

@Tag(name = "Autenticación", description = "Registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario cliente")
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "Iniciar sesión con email y contraseña")
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> body) {
        String mail = body.get("mail");
        String contraseña = body.get("contraseña");
        return ResponseEntity.ok(usuarioService.login(mail, contraseña));
    }
}