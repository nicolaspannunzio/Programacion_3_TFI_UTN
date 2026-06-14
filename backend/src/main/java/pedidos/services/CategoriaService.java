package pedidos.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pedidos.dtos.categoria.CategoriaCreate;
import pedidos.dtos.categoria.CategoriaEdit;
import pedidos.entidades.Categoria;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.CategoriaRepository;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria crear(CategoriaCreate dto) {
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .eliminado(false)
                .build();
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAllByEliminadoFalse();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findByIdOrThrow(id);
    }

    public Categoria actualizar(Long id, CategoriaEdit dto) {
        Categoria categoria = categoriaRepository.findByIdOrThrow(id);
        if (dto.getNombre() != null) categoria.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaRepository.softDelete(id);
    }
}