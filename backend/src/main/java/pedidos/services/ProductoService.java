package pedidos.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pedidos.dtos.producto.ProductoCreate;
import pedidos.dtos.producto.ProductoEdit;
import pedidos.entidades.Categoria;
import pedidos.entidades.Producto;
import pedidos.exceptions.ResourceNotFoundException;
import pedidos.repositories.CategoriaRepository;
import pedidos.repositories.ProductoRepository;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public Producto crear(ProductoCreate dto) {
        Categoria categoria = categoriaRepository.findByIdOrThrow(dto.getIdCategoria());
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .descripcion(dto.getDescripcion())
                .stock(dto.getStock())
                .imagen(dto.getImagen())
                .disponible(dto.getDisponible() != null ? dto.getDisponible() : true)
                .categoria(categoria)
                .eliminado(false)
                .build();
        return productoRepository.save(producto);
    }

    public List<Producto> listar() {
        return productoRepository.findAllByEliminadoFalse();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findByIdOrThrow(id);
    }

    public List<Producto> buscarPorCategoria(Long categoriaId) {
        categoriaRepository.findByIdOrThrow(categoriaId);
        return productoRepository.findAllByCategoriaIdAndEliminadoFalse(categoriaId);
    }

    public Producto actualizar(Long id, ProductoEdit dto) {
        Producto producto = productoRepository.findByIdOrThrow(id);
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
        if (dto.getImagen() != null) producto.setImagen(dto.getImagen());
        if (dto.getDisponible() != null) producto.setDisponible(dto.getDisponible());
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findByIdOrThrow(dto.getIdCategoria());
            producto.setCategoria(categoria);
        }
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.softDelete(id);
    }
}