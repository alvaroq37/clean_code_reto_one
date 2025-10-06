// Definición de un producto en el dominio de un e‑commerce.  Incluye
// propiedades básicas y un método para actualizar el stock.

export class Producto {
  constructor(
    public id: string,
    public nombre: string,
    public precio: number,
    public stock: number
  ) {}

  actualizarStock(nuevaCantidad: number): void {
    this.stock = nuevaCantidad;
  }
}