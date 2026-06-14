package pedidos.dtos.usuario;

import lombok.Data;
import pedidos.enums.Rol;

@Data
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private Rol rol;
}