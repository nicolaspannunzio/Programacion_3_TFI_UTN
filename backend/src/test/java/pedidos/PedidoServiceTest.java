package pedidos;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pedidos.dtos.detallePedido.DetallePedidoCreate;
import pedidos.dtos.pedido.PedidoCreate;
import pedidos.dtos.pedido.PedidoEdit;
import pedidos.entidades.Categoria;
import pedidos.entidades.Pedido;
import pedidos.entidades.Producto;
import pedidos.entidades.Usuario;
import pedidos.enums.Estado;
import pedidos.enums.FormaPago;
import pedidos.enums.Rol;
import pedidos.exceptions.BusinessException;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.PedidoRepository;
import pedidos.repositories.ProductoRepository;
import pedidos.repositories.UsuarioRepository;
import pedidos.services.PedidoService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario crearUsuario() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .mail("juan@mail.com")
                .contraseña("123456")
                .rol(Rol.USUARIO)
                .eliminado(false)
                .build();
    }

    private Producto crearProducto(String nombre, Double precio, Integer stock) {
        return Producto.builder()
                .nombre(nombre)
                .precio(precio)
                .stock(stock)
                .disponible(true)
                .eliminado(false)
                .categoria(Categoria.builder().nombre("Hamburguesas").build())
                .build();
    }

    @Test
    void crear_conDatosValidos_deberiaCrearPedidoYReducirStock() {
        Usuario usuario = crearUsuario();
        Producto producto = crearProducto("Hamburguesa triple", 25000.0, 10);

        DetallePedidoCreate detalle = new DetallePedidoCreate();
        detalle.setIdProducto(1L);
        detalle.setCantidad(2);

        PedidoCreate dto = new PedidoCreate();
        dto.setIdUsuario(1L);
        dto.setEstado(Estado.PENDIENTE);
        dto.setFormaPago(FormaPago.EFECTIVO);
        dto.setDetallePedido(List.of(detalle));

        Pedido pedidoGuardado = Pedido.builder()
                .usuario(usuario)
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.EFECTIVO)
                .eliminado(false)
                .build();

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuario);
        when(productoRepository.findByIdOrThrow(1L)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        Pedido resultado = pedidoService.crear(dto);

        assertNotNull(resultado);
        assertEquals(Estado.PENDIENTE, resultado.getEstado());
        assertEquals(8, producto.getStock());
        verify(productoRepository, times(1)).save(producto);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crear_conProductoNoDisponible_deberiaLanzarExcepcion() {
        Usuario usuario = crearUsuario();
        Producto producto = Producto.builder()
                .nombre("Producto agotado")
                .precio(1000.0)
                .stock(0)
                .disponible(false)
                .eliminado(false)
                .build();

        DetallePedidoCreate detalle = new DetallePedidoCreate();
        detalle.setIdProducto(1L);
        detalle.setCantidad(1);

        PedidoCreate dto = new PedidoCreate();
        dto.setIdUsuario(1L);
        dto.setEstado(Estado.PENDIENTE);
        dto.setFormaPago(FormaPago.EFECTIVO);
        dto.setDetallePedido(List.of(detalle));

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuario);
        when(productoRepository.findByIdOrThrow(1L)).thenReturn(producto);

        assertThrows(BusinessException.class, () -> {
            pedidoService.crear(dto);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crear_conStockInsuficiente_deberiaLanzarExcepcion() {
        Usuario usuario = crearUsuario();
        Producto producto = crearProducto("Hamburguesa triple", 25000.0, 5);

        DetallePedidoCreate detalle = new DetallePedidoCreate();
        detalle.setIdProducto(1L);
        detalle.setCantidad(10);

        PedidoCreate dto = new PedidoCreate();
        dto.setIdUsuario(1L);
        dto.setEstado(Estado.PENDIENTE);
        dto.setFormaPago(FormaPago.EFECTIVO);
        dto.setDetallePedido(List.of(detalle));

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuario);
        when(productoRepository.findByIdOrThrow(1L)).thenReturn(producto);

        assertThrows(BusinessException.class, () -> {
            pedidoService.crear(dto);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crear_sinDetalles_deberiaLanzarExcepcion() {
        Usuario usuario = crearUsuario();

        PedidoCreate dto = new PedidoCreate();
        dto.setIdUsuario(1L);
        dto.setEstado(Estado.PENDIENTE);
        dto.setFormaPago(FormaPago.EFECTIVO);
        dto.setDetallePedido(List.of());

        when(usuarioRepository.findByIdOrThrow(1L)).thenReturn(usuario);

        assertThrows(BusinessException.class, () -> {
            pedidoService.crear(dto);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crear_conUsuarioInexistente_deberiaLanzarExcepcion() {
        PedidoCreate dto = new PedidoCreate();
        dto.setIdUsuario(999L);
        dto.setEstado(Estado.PENDIENTE);
        dto.setFormaPago(FormaPago.EFECTIVO);
        dto.setDetallePedido(List.of());

        when(usuarioRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.crear(dto);
        });
    }

    @Test
    void listar_deberiaRetornarTodosLosPedidosActivos() {
        Pedido p1 = Pedido.builder().estado(Estado.PENDIENTE).eliminado(false).build();
        Pedido p2 = Pedido.builder().estado(Estado.CONFIRMADO).eliminado(false).build();

        when(pedidoRepository.findAllByEliminadoFalse()).thenReturn(List.of(p1, p2));

        List<Pedido> resultado = pedidoService.listar();

        assertEquals(2, resultado.size());
        verify(pedidoRepository, times(1)).findAllByEliminadoFalse();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarPedido() {
        Pedido pedido = Pedido.builder()
                .estado(Estado.PENDIENTE)
                .eliminado(false)
                .build();

        when(pedidoRepository.findByIdOrThrow(1L)).thenReturn(pedido);

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(Estado.PENDIENTE, resultado.getEstado());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(pedidoRepository.findByIdOrThrow(999L))
                .thenThrow(new ResourceNotFoundException("Entidad con id 999 no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.buscarPorId(999L);
        });
    }

    @Test
    void actualizarEstado_deberiaModificarEstadoCorrectamente() {
        Pedido pedidoExistente = Pedido.builder()
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.EFECTIVO)
                .eliminado(false)
                .build();

        PedidoEdit dto = new PedidoEdit();
        dto.setEstado(Estado.CONFIRMADO);

        when(pedidoRepository.findByIdOrThrow(1L)).thenReturn(pedidoExistente);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoExistente);

        Pedido resultado = pedidoService.actualizarEstado(1L, dto);

        assertEquals(Estado.CONFIRMADO, resultado.getEstado());
        assertEquals(FormaPago.EFECTIVO, resultado.getFormaPago());
    }

    @Test
    void eliminar_deberiaLlamarSoftDelete() {
        doNothing().when(pedidoRepository).softDelete(1L);

        pedidoService.eliminar(1L);

        verify(pedidoRepository, times(1)).softDelete(1L);
    }
}