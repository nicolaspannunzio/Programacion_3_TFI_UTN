package pedidos.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entidad, Long id) {
        super("Entidad " + entidad + " con id " + id + " no encontrado");
    }

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}