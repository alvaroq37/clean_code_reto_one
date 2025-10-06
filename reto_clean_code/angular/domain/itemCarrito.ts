// Representa un ítem en el carrito de compras: un producto y la cantidad.

import { Producto } from './producto';

export class ItemCarrito {
  constructor(public producto: Producto, public cantidad: number) {}
}