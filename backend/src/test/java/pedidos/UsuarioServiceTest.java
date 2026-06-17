package pedidos;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedidos.dtos.usuario.UsuarioCreate;
import pedidos.dtos.usuario.UsuarioEdit;
import pedidos.entidades.Usuario;
import pedidos.enums.Rol;
import pedidos.exceptions.BusinessException;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.UsuarioRepository;
import pedidos.services.UsuarioService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crear_conEmailNuevo_deberiaRetornarUsuario() {
        UsuarioCreate dto = new UsuarioCreate();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setMail("juan@mail.com");
        dto.setCelular("1234567890");
        dto.setContraseña("123456");

        Usuario usuarioGuardado = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .mail("juan@mail.com")
                .celular("1234567890")
                .contraseña("123456")
                .rol(Rol.USUARIO)
                .eliminado(false)
                .build();

        when(usuarioRepository.existsByMail("juan@mail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals(Rol.USUARIO, resultado.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crear_conEmailDuplicado_deberiaLanzarExcepcion() {
        UsuarioCreate dto = new UsuarioCreate();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setMail("juan@mail.com");
        dto.setContraseña("123456");

        when(usuarioRepository.existsByMail("juan@mail.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            usuarioService.crear(dto);
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listar_deberiaRetornarSoloUsuariosActivos() {
        Usuario u1 = Usuario.builder().nombre("Juan").eliminado(false).build();
        Usuario u2 = Usuario.builder().nombre("Maria").eliminado(false).build();

        when(usuarioRepository.findAllByEliminadoFalse()).thenReturn(List.of(u1, u2));

        List<Usuario> resultado = usuarioService.listar();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAllByEliminadoFalse();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarUsuario() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .mail("juan@mail.com")
                .eliminado(false)
                .build();

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuario);

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(usuarioRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.buscarPorId(999L);
        });
    }

    @Test
    void login_conCredencialesCorrectas_deberiaRetornarUsuario() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .mail("juan@mail.com")
                .contraseña("123456")
                .rol(Rol.USUARIO)
                .eliminado(false)
                .build();

        when(usuarioRepository.findByMailAndEliminadoFalse("juan@mail.com"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.login("juan@mail.com", "123456");

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void login_conPasswordIncorrecta_deberiaLanzarExcepcion() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .mail("juan@mail.com")
                .contraseña("123456")
                .eliminado(false)
                .build();

        when(usuarioRepository.findByMailAndEliminadoFalse("juan@mail.com"))
                .thenReturn(Optional.of(usuario));

        assertThrows(BusinessException.class, () -> {
            usuarioService.login("juan@mail.com", "contraseñaincorrecta");
        });
    }

    @Test
    void login_conEmailInexistente_deberiaLanzarExcepcion() {
        when(usuarioRepository.findByMailAndEliminadoFalse("noexiste@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            usuarioService.login("noexiste@mail.com", "123456");
        });
    }

    @Test
    void actualizar_conEmailNuevo_deberiaActualizarCorrectamente() {
        Usuario usuarioExistente = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .mail("juan@mail.com")
                .celular("123")
                .contraseña("123456")
                .eliminado(false)
                .build();

        UsuarioEdit dto = new UsuarioEdit();
        dto.setNombre("Juan Actualizado");
        dto.setMail("juannuevo@mail.com");

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuarioExistente);
        when(usuarioRepository.existsByMail("juannuevo@mail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.actualizar(1L, dto);

        assertEquals("Juan Actualizado", resultado.getNombre());
        assertEquals("juannuevo@mail.com", resultado.getMail());
    }

    @Test
    void actualizar_conEmailDuplicado_deberiaLanzarExcepcion() {
        Usuario usuarioExistente = Usuario.builder()
                .nombre("Juan")
                .mail("juan@mail.com")
                .eliminado(false)
                .build();

        UsuarioEdit dto = new UsuarioEdit();
        dto.setMail("otro@mail.com");

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuarioExistente);
        when(usuarioRepository.existsByMail("otro@mail.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            usuarioService.actualizar(1L, dto);
        });
    }

    @Test
    void eliminar_deberiaLlamarSoftDelete() {
        doNothing().when(usuarioRepository).softDelete(1L);

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1)).softDelete(1L);
    }
}