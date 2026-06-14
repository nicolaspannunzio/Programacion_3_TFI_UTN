package pedidos.dtos.pedido;

import java.util.List;
import lombok.Data;
import pedidos.dtos.detallePedido.DetallePedidoCreate;
import pedidos.enums.Estado;
import pedidos.enums.FormaPago;

@Data
public class PedidoCreate {
    private Long idUsuario;
    private Estado estado;
    private FormaPago formaPago;
    private List<DetallePedidoCreate> detallePedido;
}