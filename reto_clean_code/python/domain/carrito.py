"""
Carrito de compras asociado a un cliente.  Permite agregar ítems y calcular
el total del pedido.
"""

from typing import List
from .item_carrito import ItemCarrito
from .producto import Producto


class Carrito:
    def __init__(self, id_cliente: str) -> None:
        self.id_cliente = id_cliente
        self.items: List[ItemCarrito] = []

    def agregar_item(self, producto: Producto, cantidad: int) -> None:
        self.items.append(ItemCarrito(producto, cantidad))

    def calcular_total(self) -> float:
        return sum(item.producto.precio * item.cantidad for item in self.items)