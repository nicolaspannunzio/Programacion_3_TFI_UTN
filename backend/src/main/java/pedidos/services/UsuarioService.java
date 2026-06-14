package pedidos.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pedidos.dtos.usuario.UsuarioCreate;
import pedidos.dtos.usuario.UsuarioEdit;
import pedidos.entidades.Usuario;
import pedidos.enums.Rol;
import pedidos.exceptions.BusinessException;
import pedidos.repositories.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario crear(UsuarioCreate dto) {
        if (usuarioRepository.existsByMail(dto.getMail())) {
            throw new BusinessException("Ya existe un usuario con el email: " + dto.getMail());
        }
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .mail(dto.getMail())
                .celular(dto.getCelular())
                .contraseña(dto.getContraseña())
                .rol(Rol.USUARIO)
                .eliminado(false)
                .build();
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAllByEliminadoFalse();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findByIdOrThrow(id);
    }

    public Usuario buscarPorMail(String mail) {
        return usuarioRepository.findByMailAndEliminadoFalse(mail)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado con mail: " + mail));
    }

    public Usuario actualizar(Long id, UsuarioEdit dto) {
        Usuario usuario = usuarioRepository.findByIdOrThrow(id);
        if (dto.getMail() != null && !dto.getMail().equals(usuario.getMail())) {
            if (usuarioRepository.existsByMail(dto.getMail())) {
                throw new BusinessException("Ya existe un usuario con el email: " + dto.getMail());
            }
            usuario.setMail(dto.getMail());
        }
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getCelular() != null) usuario.setCelular(dto.getCelular());
        if (dto.getContraseña() != null) usuario.setContraseña(dto.getContraseña());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.softDelete(id);
    }

    public Usuario login(String mail, String contraseña) {
        Usuario usuario = buscarPorMail(mail);
        if (!usuario.getContraseña().equals(contraseña)) {
            throw new BusinessException("Credenciales incorrectas");
        }
        return usuario;
    }
}