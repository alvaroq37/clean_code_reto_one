"""
Representa un ítem dentro del carrito: un producto y la cantidad deseada.
"""

from dataclasses import dataclass
from .producto import Producto


@dataclass
class ItemCarrito:
    producto: Producto
    cantidad: int