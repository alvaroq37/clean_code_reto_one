/**
 * Clase de ejemplo que viola el Principio de Responsabilidad Única (SRP) al
 * mezclar la generación, notificación y persistencia de un informe en una
 * sola clase.  Utiliza este archivo como base para refactorizar en el
 * ejercicio del paso 4.
 */
public class Informe {
    public void generarPDF() {
        // Generar un documento PDF
    }

    public void enviarEmail() {
        // Enviar el informe por correo electrónico
    }

    public void guardarEnBaseDatos() {
        // Guardar el informe en la base de datos
    }
}