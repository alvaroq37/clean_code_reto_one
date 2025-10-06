"""
Método con olores de código para generar un reporte de empleados.  Esta
función es demasiado larga y mezcla varias responsabilidades.  Debe usarse
para identificar y solucionar code smells en el paso 5.
"""

from datetime import datetime
from typing import List


class Empleado:
    def __init__(self, id: str, nombre: str, salario: float) -> None:
        self.id = id
        self.nombre = nombre
        self.salario = salario


def generar_reporte(empleados: List[Empleado]) -> str:
    lines = []
    for e in empleados:
        lines.append(f"{e.id};{e.nombre};{e.salario}\n")
    contenido = ''.join(lines)
    enviar_por_correo(contenido)
    guardar_en_archivo(contenido)
    registrar_auditoria("REPORTE", len(contenido), datetime.now())
    return contenido


def enviar_por_correo(contenido: str) -> None:
    # Simular envío de correo
    pass


def guardar_en_archivo(contenido: str) -> None:
    # Simular persistencia en archivo
    pass


def registrar_auditoria(tipo: str, longitud: int, fecha: datetime) -> None:
    # Simular registro de auditoría
    pass