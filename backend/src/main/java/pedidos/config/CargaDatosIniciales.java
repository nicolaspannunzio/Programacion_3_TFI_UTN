package pedidos.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pedidos.entidades.Usuario;
import pedidos.enums.Rol;
import pedidos.repositories.UsuarioRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CargaDatosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Sistema")
                    .mail("admin@admin.com")
                    .contraseña("123456")
                    .celular("")
                    .rol(Rol.ADMIN)
                    .eliminado(false)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin@admin.com / 123456");
        } else {
            log.info("Ya existen usuarios en la base de datos, no se crea admin.");
        }
    }
}