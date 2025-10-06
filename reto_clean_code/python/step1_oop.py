"""
Ejemplo orientado a objetos para realizar una transferencia bancaria.
Las clases encapsulan el estado de las cuentas y la lógica de la operación.
"""


class CuentaBancaria:
    def __init__(self, titular: str, saldo: float) -> None:
        self.titular = titular
        self.saldo = saldo

    def retirar(self, monto: float) -> bool:
        if monto <= self.saldo:
            self.saldo -= monto
            return True
        return False

    def depositar(self, monto: float) -> None:
        self.saldo += monto


class Transferencia:
    def realizar(self, origen: 'CuentaBancaria', destino: 'CuentaBancaria', monto: float) -> None:
        if origen.retirar(monto):
            destino.depositar(monto)
            print(f"Transferencia de {monto} de {origen.titular} a {destino.titular}")
        else:
            print("Saldo insuficiente")


if __name__ == "__main__":
    cuenta_a = CuentaBancaria('A', 1000)
    cuenta_b = CuentaBancaria('B', 500)
    transferencia = Transferencia()
    transferencia.realizar(cuenta_a, cuenta_b, 200)
    print(cuenta_a.saldo, cuenta_b.saldo)