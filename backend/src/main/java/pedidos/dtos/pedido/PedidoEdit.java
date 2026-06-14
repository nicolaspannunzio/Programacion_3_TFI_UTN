package pedidos.dtos.pedido;

import lombok.Data;
import pedidos.enums.Estado;
import pedidos.enums.FormaPago;

@Data
public class PedidoEdit {
    private Estado estado;
    private FormaPago formaPago;
}