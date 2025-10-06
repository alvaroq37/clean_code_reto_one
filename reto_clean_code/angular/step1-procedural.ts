// Script procedural que utiliza variables globales para realizar una
// transferencia entre dos cuentas.  Este archivo sirve como punto de partida
// para refactorizar a un enfoque orientado a objetos.

let saldoCuentaA = 1000;
let saldoCuentaB = 500;

export function transferir(origen: 'A' | 'B', monto: number): void {
  if (origen === 'A') {
    if (saldoCuentaA >= monto) {
      saldoCuentaA -= monto;
      saldoCuentaB += monto;
      console.log('Transferencia exitosa.');
    } else {
      console.log('Saldo insuficiente.');
    }
  } else {
    if (saldoCuentaB >= monto) {
      saldoCuentaB -= monto;
      saldoCuentaA += monto;
      console.log('Transferencia exitosa.');
    } else {
      console.log('Saldo insuficiente.');
    }
  }
}