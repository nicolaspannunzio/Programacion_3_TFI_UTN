package pedidos.services;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pedidos.dtos.detallePedido.DetallePedidoCreate;
import pedidos.dtos.pedido.PedidoCreate;
import pedidos.dtos.pedido.PedidoEdit;
import pedidos.entidades.Pedido;
import pedidos.entidades.Producto;
import pedidos.entidades.Usuario;
import pedidos.exceptions.BusinessException;
import pedidos.repositories.PedidoRepository;
import pedidos.repositories.ProductoRepository;
import pedidos.repositories.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(rollbackOn = Exception.class)
    public Pedido crear(PedidoCreate dto) {
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findByIdOrThrow(dto.getIdUsuario());

        // Validar que hay al menos un detalle
        if (dto.getDetallePedido() == null || dto.getDetallePedido().isEmpty()) {
            throw new BusinessException("El pedido debe tener al menos un producto");
        }

        // Crear el pedido base
        Pedido pedido = Pedido.builder()
                .fecha(LocalDate.now())
                .estado(dto.getEstado())
                .formaPago(dto.getFormaPago())
                .usuario(usuario)
                .eliminado(false)
                .build();

        // Procesar cada detalle: validar y reducir stock
        for (DetallePedidoCreate detalleDto : dto.getDetallePedido()) {
            Producto producto = productoRepository.findByIdOrThrow(detalleDto.getIdProducto());

            if (producto.getDisponible() == null || !producto.getDisponible()) {
                throw new BusinessException("El producto '" + producto.getNombre()
                        + "' no está disponible para la venta");
            }

            producto.reducirStock(detalleDto.getCantidad());
            productoRepository.save(producto);

            pedido.addDetallePedido(detalleDto.getCantidad(), producto);
        }

        // Calcular total
        pedido.calcularTotal();

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAllByEliminadoFalse();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findByIdOrThrow(id);
    }

    public List<Pedido> buscarPorUsuario(Long usuarioId) {
        usuarioRepository.findByIdOrThrow(usuarioId);
        return pedidoRepository.findAllByUsuarioIdAndEliminadoFalse(usuarioId);
    }

    @Transactional(rollbackOn = Exception.class)
    public Pedido actualizarEstado(Long id, PedidoEdit dto) {
        Pedido pedido = pedidoRepository.findByIdOrThrow(id);
        if (dto.getEstado() != null) pedido.setEstado(dto.getEstado());
        if (dto.getFormaPago() != null) pedido.setFormaPago(dto.getFormaPago());
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        pedidoRepository.softDelete(id);
    }
}