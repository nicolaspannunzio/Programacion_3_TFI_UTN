package pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedidos.dtos.producto.ProductoCreate;
import pedidos.dtos.producto.ProductoEdit;
import pedidos.entidades.Categoria;
import pedidos.entidades.Producto;
import pedidos.exceptions.BusinessException;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.CategoriaRepository;
import pedidos.repositories.ProductoRepository;
import pedidos.services.ProductoService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void crear_conCategoriaValida_deberiaRetornarProducto() {
        Categoria categoria = Categoria.builder()
                .nombre("Hamburguesas")
                .eliminado(false)
                .build();

        ProductoCreate dto = new ProductoCreate();
        dto.setNombre("Hamburguesa triple");
        dto.setPrecio(25000.0);
        dto.setDescripcion("Hamburguesa triple carne");
        dto.setStock(10);
        dto.setDisponible(true);
        dto.setIdCategoria(1L);

        Producto productoGuardado = Producto.builder()
                .nombre("Hamburguesa triple")
                .precio(25000.0)
                .stock(10)
                .disponible(true)
                .categoria(categoria)
                .eliminado(false)
                .build();

        when(categoriaRepository.findByIdOrThrow(1L)).thenReturn(categoria);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        Producto resultado = productoService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Hamburguesa triple", resultado.getNombre());
        assertEquals(25000.0, resultado.getPrecio());
        assertEquals("Hamburguesas", resultado.getCategoria().getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void crear_conCategoriaInexistente_deberiaLanzarExcepcion() {
        ProductoCreate dto = new ProductoCreate();
        dto.setNombre("Producto test");
        dto.setPrecio(1000.0);
        dto.setStock(5);
        dto.setIdCategoria(999L);

        when(categoriaRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.crear(dto);
        });

        verify(productoRepository, never()).save(any());
    }

    @Test
    void listar_deberiaRetornarSoloProductosActivos() {
        Producto p1 = Producto.builder().nombre("Producto 1").eliminado(false).build();
        Producto p2 = Producto.builder().nombre("Producto 2").eliminado(false).build();

        when(productoRepository.findAllByEliminadoFalse()).thenReturn(List.of(p1, p2));

        List<Producto> resultado = productoService.listar();

        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAllByEliminadoFalse();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarProducto() {
        Producto producto = Producto.builder()
                .nombre("Hamburguesa triple")
                .precio(25000.0)
                .eliminado(false)
                .build();

        when(productoRepository.findByIdOrThrow(1L)).thenReturn(producto);

        Producto resultado = productoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Hamburguesa triple", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(productoRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.buscarPorId(999L);
        });
    }

    @Test
    void actualizar_soloDeberiaCambiarCamposEnviados() {
        Categoria categoriaOriginal = Categoria.builder()
                .nombre("Hamburguesas")
                .eliminado(false)
                .build();

        Producto productoExistente = Producto.builder()
                .nombre("Nombre original")
                .precio(1000.0)
                .stock(5)
                .disponible(true)
                .categoria(categoriaOriginal)
                .eliminado(false)
                .build();

        ProductoEdit dto = new ProductoEdit();
        dto.setPrecio(2000.0);

        when(productoRepository.findByIdOrThrow(1L)).thenReturn(productoExistente);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoExistente);

        Producto resultado = productoService.actualizar(1L, dto);

        assertEquals(2000.0, resultado.getPrecio());
        assertEquals("Nombre original", resultado.getNombre());
        assertEquals(5, resultado.getStock());
    }

    @Test
    void reducirStock_conStockSuficiente_deberiaReducirCorrectamente() {
        Producto producto = Producto.builder()
                .nombre("Hamburguesa triple")
                .stock(10)
                .disponible(true)
                .eliminado(false)
                .build();

        producto.reducirStock(3);

        assertEquals(7, producto.getStock());
    }

    @Test
    void reducirStock_conStockInsuficiente_deberiaLanzarExcepcion() {
        Producto producto = Producto.builder()
                .nombre("Hamburguesa triple")
                .stock(5)
                .disponible(true)
                .eliminado(false)
                .build();

        assertThrows(BusinessException.class, () -> {
            producto.reducirStock(10);
        });
    }

    @Test
    void eliminar_deberiaLlamarSoftDelete() {
        doNothing().when(productoRepository).softDelete(1L);

        productoService.eliminar(1L);

        verify(productoRepository, times(1)).softDelete(1L);
    }
}