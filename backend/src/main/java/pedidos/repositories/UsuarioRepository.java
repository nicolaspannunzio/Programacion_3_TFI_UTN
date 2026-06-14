package pedidos.repositories;

import java.util.Optional;
import pedidos.entidades.Usuario;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    Optional<Usuario> findByMailAndEliminadoFalse(String mail);
    boolean existsByMail(String mail);
}