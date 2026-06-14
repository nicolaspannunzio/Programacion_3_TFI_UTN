package pedidos.repositories;

import java.util.List;
import pedidos.entidades.Producto;

public interface ProductoRepository extends BaseRepository<Producto, Long> {

    List<Producto> findAllByCategoriaIdAndEliminadoFalse(Long categoriaId);
}