package pedidos.repositories;

import java.util.List;
import pedidos.entidades.Pedido;

public interface PedidoRepository extends BaseRepository<Pedido, Long> {

    List<Pedido> findAllByUsuarioIdAndEliminadoFalse(Long usuarioId);
}