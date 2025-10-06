"""
Representa un pedido dentro del sistema.  Contiene un identificador,
el total y un estado que puede cambiarse a confirmado o cancelado.
"""


class Pedido:
    def __init__(self, id: str, total: float) -> None:
        self.id = id
        self.total = total
        self.estado = "PENDIENTE"

    def confirmar(self) -> None:
        self.estado = "CONFIRMADO"

    def cancelar(self) -> None:
        self.estado = "CANCELADO"