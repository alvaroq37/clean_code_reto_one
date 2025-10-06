"""
Refactorización del ejercicio de Informe aplicando los principios SOLID.
Las responsabilidades se separan en interfaces y clases específicas para
generar informes, notificar y persistir la información.  Además, se
coordina el flujo desde un servicio que inyecta estas dependencias.
"""

from dataclasses import dataclass


class GeneradorInforme:
    def generar(self, data: 'InformeData') -> None:
        raise NotImplementedError()


class InformePDF(GeneradorInforme):
    def generar(self, data: 'InformeData') -> None:
        # Implementar generación de PDF
        pass


class Notificador:
    def notificar(self, data: 'InformeData') -> None:
        raise NotImplementedError()


class EmailNotificador(Notificador):
    def notificar(self, data: 'InformeData') -> None:
        # Implementar envío de correo electrónico
        pass


class Repositorio:
    def guardar(self, data: 'InformeData') -> None:
        raise NotImplementedError()


class InformeRepositorio(Repositorio):
    def guardar(self, data: 'InformeData') -> None:
        # Implementar persistencia en la base de datos
        pass


class InformeService:
    def __init__(self, generador: GeneradorInforme, notificador: Notificador, repositorio: Repositorio) -> None:
        self.generador = generador
        self.notificador = notificador
        self.repositorio = repositorio

    def procesar(self, data: 'InformeData') -> None:
        self.generador.generar(data)
        self.notificador.notificar(data)
        self.repositorio.guardar(data)


@dataclass
class InformeData:
    # Definir campos del informe según sea necesario
    contenido: str = ""