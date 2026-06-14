package pedidos.dtos.detallePedido;

import lombok.Data;
import pedidos.dtos.producto.ProductoDto;

@Data
public class DetallePedidoDto {
    private int cantidad;
    private Double subtotal;
    private ProductoDto producto;
}