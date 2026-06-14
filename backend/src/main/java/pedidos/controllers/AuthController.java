package pedidos.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedidos.dtos.usuario.UsuarioCreate;
import pedidos.entidades.Usuario;
import pedidos.services.UsuarioService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> body) {
        String mail = body.get("mail");
        String contraseña = body.get("contraseña");
        return ResponseEntity.ok(usuarioService.login(mail, contraseña));
    }
}