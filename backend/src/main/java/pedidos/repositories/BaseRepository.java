package pedidos.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pedidos.entidades.Base;
import pedidos.exceptions.ResourceNotFoundException;

@NoRepositoryBean
public interface BaseRepository<E extends Base, ID> extends JpaRepository<E, ID> {

    List<E> findAllByEliminadoFalse();

    default List<E> findAll() {
        return findAllByEliminadoFalse();
    }

    default E findByIdOrThrow(Long id) {
        return findAllByEliminadoFalse()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Entidad con id " + id + " no encontrado"));    }

    default void softDelete(Long id) {
        E entidad = findByIdOrThrow(id);
        entidad.setEliminado(true);
        save(entidad);
    }
}