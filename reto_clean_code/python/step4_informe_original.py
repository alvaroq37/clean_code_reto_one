"""
Ejemplo de una clase que viola el Principio de Responsabilidad Única al
mezclar generación, notificación y persistencia en una sola entidad.
Utilízala como base para refactorizar en el paso 4.
"""


class Informe:
    def generar_pdf(self) -> None:
        # Generar PDF
        pass

    def enviar_email(self) -> None:
        # Enviar correo
        pass

    def guardar_en_bd(self) -> None:
        # Guardar en base de datos
        pass