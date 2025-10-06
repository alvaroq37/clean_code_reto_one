package domain;

/**
 * Representa un pedido que puede confirmarse o cancelarse.  Se asocia con el
 * carrito y el pago, pero para simplificar no incluye esa lógica aquí.
 */
public class Pedido {
    private String id;
    private String estado = "PENDIENTE";
    private double total;

    public Pedido(String id, double total) {
        this.id = id;
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void confirmar() {
        this.estado = "CONFIRMADO";
    }

    public void cancelar() {
        this.estado = "CANCELADO";
    }
}