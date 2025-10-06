// Refactorización orientada a objetos de la transferencia bancaria.  Se
// introducen clases para encapsular el estado de las cuentas y la lógica
// de la operación.

export class CuentaBancaria {
  constructor(public titular: string, public saldo: number) {}

  retirar(monto: number): boolean {
    if (monto <= this.saldo) {
      this.saldo -= monto;
      return true;
    }
    return false;
  }

  depositar(monto: number): void {
    this.saldo += monto;
  }
}

export class Transferencia {
  realizar(origen: CuentaBancaria, destino: CuentaBancaria, monto: number): void {
    if (origen.retirar(monto)) {
      destino.depositar(monto);
      console.log(`Transferencia de ${monto} de ${origen.titular} a ${destino.titular}`);
    } else {
      console.log('Saldo insuficiente.');
    }
  }
}