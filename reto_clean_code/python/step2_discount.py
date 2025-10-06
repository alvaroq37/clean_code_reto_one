"""
Módulo para calcular descuentos según el tipo de cliente.  Esta función se
utiliza en el paso 2 para practicar TDD y refactorización.
"""


def calcular_descuento(precio: float, tipo_cliente: str) -> float:
    descuentos = {
        'preferente': 0.1,
        'vip': 0.2,
    }
    return precio * (1 - descuentos.get(tipo_cliente, 0))