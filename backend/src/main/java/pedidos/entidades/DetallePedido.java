package pedidos.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}