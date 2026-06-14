package pedidos.dtos.producto;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private Boolean disponible;
}