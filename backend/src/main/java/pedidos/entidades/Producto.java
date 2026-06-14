package pedidos.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pedidos.exceptions.BusinessException;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Producto extends Base {

    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;
    private String imagen;
    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    public void reducirStock(int cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new BusinessException("Stock insuficiente para '" + this.nombre
                    + "'. Disponible: " + this.stock + ", Solicitado: " + cantidad);
        }
        this.stock -= cantidad;
    }
}