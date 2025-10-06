"""
Script procedural para transferir fondos entre dos cuentas.  Define variables
globales y una función que modifica estas variables directamente.  Se utiliza
como base para refactorizar a un diseño orientado a objetos en el paso 1.
"""

saldo_cuenta_a = 1000
saldo_cuenta_b = 500


def transferir(origen: str, destino: str, monto: float) -> None:
    global saldo_cuenta_a, saldo_cuenta_b
    if origen == 'A':
        if saldo_cuenta_a >= monto:
            saldo_cuenta_a -= monto
            saldo_cuenta_b += monto
            print("Transferencia exitosa.")
        else:
            print("Saldo insuficiente.")
    elif origen == 'B':
        if saldo_cuenta_b >= monto:
            saldo_cuenta_b -= monto
            saldo_cuenta_a += monto
            print("Transferencia exitosa.")
        else:
            print("Saldo insuficiente.")


if __name__ == "__main__":
    transferir('A', 'B', 200)
    print(saldo_cuenta_a, saldo_cuenta_b)