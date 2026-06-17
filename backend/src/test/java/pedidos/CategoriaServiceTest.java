package pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedidos.dtos.categoria.CategoriaCreate;
import pedidos.dtos.categoria.CategoriaEdit;
import pedidos.entidades.Categoria;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.CategoriaRepository;
import pedidos.services.CategoriaService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void crear_deberiaRetornarCategoriaCreada() {
        CategoriaCreate dto = new CategoriaCreate();
        dto.setNombre("Hamburguesas");
        dto.setDescripcion("Las mejores hamburguesas");

        Categoria categoriaGuardada = Categoria.builder()
                .nombre("Hamburguesas")
                .descripcion("Las mejores hamburguesas")
                .eliminado(false)
                .build();

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        Categoria resultado = categoriaService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Hamburguesas", resultado.getNombre());
        assertEquals("Las mejores hamburguesas", resultado.getDescripcion());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void listar_deberiaRetornarListaDeCategorias() {
        Categoria cat1 = Categoria.builder().nombre("Hamburguesas").eliminado(false).build();
        Categoria cat2 = Categoria.builder().nombre("Pizzas").eliminado(false).build();

        when(categoriaRepository.findAllByEliminadoFalse()).thenReturn(List.of(cat1, cat2));

        List<Categoria> resultado = categoriaService.listar();

        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAllByEliminadoFalse();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarCategoria() {
        Categoria categoria = Categoria.builder()
                .nombre("Hamburguesas")
                .eliminado(false)
                .build();

        when(categoriaRepository.findByIdOrThrow(1L)).thenReturn(categoria);

        Categoria resultado = categoriaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Hamburguesas", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(categoriaRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.buscarPorId(999L);
        });
    }

    @Test
    void actualizar_deberiaModificarSoloLosCamposEnviados() {
        Categoria categoriaExistente = Categoria.builder()
                .nombre("Nombre original")
                .descripcion("Descripcion original")
                .eliminado(false)
                .build();

        CategoriaEdit dto = new CategoriaEdit();
        dto.setNombre("Nombre actualizado");

        when(categoriaRepository.findByIdOrThrow(1L)).thenReturn(categoriaExistente);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaExistente);

        Categoria resultado = categoriaService.actualizar(1L, dto);

        assertEquals("Nombre actualizado", resultado.getNombre());
        assertEquals("Descripcion original", resultado.getDescripcion());
    }

    @Test
    void eliminar_deberiaLlamarSoftDelete() {
        doNothing().when(categoriaRepository).softDelete(1L);

        categoriaService.eliminar(1L);

        verify(categoriaRepository, times(1)).softDelete(1L);
    }
}