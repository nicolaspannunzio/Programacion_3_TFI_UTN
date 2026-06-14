package pedidos.dtos.categoria;

import lombok.Data;

@Data
public class CategoriaDto {
    private Long id;
    private String nombre;
    private String descripcion;
}