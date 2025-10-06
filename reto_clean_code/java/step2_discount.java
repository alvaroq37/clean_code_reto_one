/**
 * Clase utilitaria para calcular descuentos según el tipo de cliente.
 */
public class DiscountCalculator {
    public static double calcularDescuento(double precio, String tipoCliente) {
        switch (tipoCliente) {
            case "preferente":
                return precio * 0.9;
            case "vip":
                return precio * 0.8;
            default:
                return precio;
        }
    }
}