package pedidos.dtos.usuario;

import lombok.Data;
import pedidos.enums.Rol;

@Data
public class UsuarioCreate {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contraseña;
    private Rol rol;
}