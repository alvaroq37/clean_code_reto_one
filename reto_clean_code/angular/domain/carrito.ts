// Carrito de compras asociado a un cliente.  Permite agregar ítems y calcular
// el total del pedido.

import { ItemCarrito } from './itemCarrito';
import { Producto } from './producto';

export class Carrito {
  public items: ItemCarrito[] = [];

  constructor(public idCliente: string) {}

  agregarItem(producto: Producto, cantidad: number): void {
    this.items.push(new ItemCarrito(producto, cantidad));
  }

  calcularTotal(): number {
    return this.items.reduce((acc, item) => acc + item.producto.precio * item.cantidad, 0);
  }
}