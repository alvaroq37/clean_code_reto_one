"""
Representa un pago de un pedido.  La lógica de procesamiento se deja
como placeholder para que los participantes la implementen según sus
requerimientos.
"""


class Pago:
    def __init__(self, id_pedido: str, monto: float, medio_pago: str) -> None:
        self.id_pedido = id_pedido
        self.monto = monto
        self.medio_pago = medio_pago

    def procesar(self) -> None:
        # Lógica de procesamiento de pago (placeholder)
        pass