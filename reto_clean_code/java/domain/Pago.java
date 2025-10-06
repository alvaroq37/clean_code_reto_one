package domain;

/**
 * Representa un pago asociado a un pedido.  Este objeto puede procesar un
 * cobro utilizando el medio de pago seleccionado.
 */
public class Pago {
    private String idPedido;
    private double monto;
    private String medioPago;

    public Pago(String idPedido, double monto, String medioPago) {
        this.idPedido = idPedido;
        this.monto = monto;
        this.medioPago = medioPago;
    }

    public void procesar() {
        // Lógica de procesamiento de pago (placeholder)
    }
}