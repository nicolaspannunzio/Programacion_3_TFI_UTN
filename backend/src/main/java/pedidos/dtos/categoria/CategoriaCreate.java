package pedidos.dtos.categoria;

import lombok.Data;

@Data
public class CategoriaCreate {
    private String nombre;
    private String descripcion;
}