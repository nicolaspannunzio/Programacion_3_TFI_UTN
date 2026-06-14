package pedidos.dtos.pedido;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import pedidos.enums.Estado;
import pedidos.enums.FormaPago;
import pedidos.dtos.detallePedido.DetallePedidoDto;

@Data
public class PedidoDto {
    private Long id;
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedidoDto> detalles;
}