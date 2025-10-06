// Representa un pedido que puede confirmarse o cancelarse.  No incluye la
// relación con el carrito ni el pago para simplificar.

export class Pedido {
  public estado = 'PENDIENTE';

  constructor(public id: string, public total: number) {}

  confirmar(): void {
    this.estado = 'CONFIRMADO';
  }

  cancelar(): void {
    this.estado = 'CANCELADO';
  }
}