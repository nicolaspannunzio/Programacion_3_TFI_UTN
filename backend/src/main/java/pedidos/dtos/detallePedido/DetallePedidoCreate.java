package pedidos.dtos.detallePedido;

import lombok.Data;

@Data
public class DetallePedidoCreate {
    private Long idProducto;
    private Integer cantidad;
}