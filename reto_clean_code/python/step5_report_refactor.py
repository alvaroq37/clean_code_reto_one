"""
Refactorización del reporte de empleados en Python.  Se dividen las
responsabilidades en componentes más simples para aplicar KISS y DRY.
"""

from datetime import datetime
from typing import List
from dataclasses import dataclass


@dataclass
class Empleado:
    id: str
    nombre: str
    salario: float


class FormateadorReporte:
    def formatear(self, empleados: List[Empleado]) -> str:
        raise NotImplementedError()


class FormateadorCSV(FormateadorReporte):
    def formatear(self, empleados: List[Empleado]) -> str:
        return ''.join(f"{e.id};{e.nombre};{e.salario}\n" for e in empleados)


class NotificadorReporte:
    def enviar(self, contenido: str) -> None:
        raise NotImplementedError()


class EmailNotificadorReporte(NotificadorReporte):
    def enviar(self, contenido: str) -> None:
        # Simular envío de correo
        pass


class PersistenciaReporte:
    def guardar(self, contenido: str) -> None:
        raise NotImplementedError()


class ArchivoPersistenciaReporte(PersistenciaReporte):
    def guardar(self, contenido: str) -> None:
        # Simular persistencia en archivo o base de datos
        pass


class Auditor:
    def registrar(self, tipo: str, longitud: int, fecha: datetime) -> None:
        raise NotImplementedError()


class AuditorSimple(Auditor):
    def registrar(self, tipo: str, longitud: int, fecha: datetime) -> None:
        # Simular registro de auditoría
        pass


class ReporteEmpleadosService:
    def __init__(self, formateador: FormateadorReporte, notificador: NotificadorReporte,
                 persistencia: PersistenciaReporte, auditor: Auditor) -> None:
        self.formateador = formateador
        self.notificador = notificador
        self.persistencia = persistencia
        self.auditor = auditor

    def generar_reporte(self, empleados: List[Empleado]) -> str:
        contenido = self.formateador.formatear(empleados)
        self.notificador.enviar(contenido)
        self.persistencia.guardar(contenido)
        self.auditor.registrar("REPORTE", len(contenido), datetime.now())
        return contenido