"""
Modelo de dominio para un producto dentro del catálogo.  Incluye atributos
básicos y un método para actualizar el stock.
"""

from dataclasses import dataclass


@dataclass
class Producto:
    id: str
    nombre: str
    precio: float
    stock: int

    def actualizar_stock(self, nueva_cantidad: int) -> None:
        self.stock = nueva_cantidad