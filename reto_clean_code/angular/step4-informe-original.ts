// Clase que viola el Principio de Responsabilidad Única al combinar
// generación, notificación y persistencia en un único servicio.  Utiliza
// este archivo para realizar la refactorización en el paso 4.

export class Informe {
  generarPdf(): void {
    // Generar PDF
  }

  enviarEmail(): void {
    // Enviar por correo electrónico
  }

  guardarEnBaseDatos(): void {
    // Guardar en la base de datos
  }
}