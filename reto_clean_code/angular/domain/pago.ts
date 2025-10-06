// Representa un pago asociado a un pedido.  Contiene la lógica de
// procesamiento como placeholder para futuras implementaciones.

export class Pago {
  constructor(public idPedido: string, public monto: number, public medioPago: string) {}

  procesar(): void {
    // Lógica de procesamiento del pago (placeholder)
  }
}