package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Carrito de compras asociado a un cliente.  Permite agregar items y calcular el total.
 */
public class Carrito {
    private String idCliente;
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito(String idCliente) {
        this.idCliente = idCliente;
    }

    public void agregarItem(Producto producto, int cantidad) {
        items.add(new ItemCarrito(producto, cantidad));
    }

    public double calcularTotal() {
        return items.stream()
                .mapToDouble(i -> i.getProducto().getPrecio() * i.getCantidad())
                .sum();
    }
}