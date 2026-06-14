package pedidos.dtos.producto;

import lombok.Data;

@Data
public class ProductoCreate {
    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;
    private String imagen;
    private Boolean disponible;
    private Long idCategoria;
}