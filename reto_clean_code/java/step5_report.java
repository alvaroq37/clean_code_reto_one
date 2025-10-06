import java.util.Date;
import java.util.List;

/**
 * Método con varios olores de código: es demasiado largo, mezcla diversas
 * responsabilidades (generación, notificación, persistencia) y no sigue los
 * principios de diseño.  Utiliza este archivo para identificar y corregir
 * dichos problemas en tu refactorización.
 */
public class ReporteEmpleados {
    public String generarReporte(List<Empleado> empleados) {
        StringBuilder sb = new StringBuilder();
        for (Empleado e : empleados) {
            sb.append(e.getId()).append(";")
              .append(e.getNombre()).append(";")
              .append(e.getSalario()).append("\n");
        }
        enviarPorCorreo(sb.toString());
        guardarEnArchivo(sb.toString());
        registrarAuditoria("REPORTE", sb.length(), new Date());
        return sb.toString();
    }

    private void enviarPorCorreo(String contenido) {
        // Lógica para enviar el reporte por correo
    }

    private void guardarEnArchivo(String contenido) {
        // Lógica para guardar el reporte en un archivo
    }

    private void registrarAuditoria(String tipo, int longitud, Date fecha) {
        // Lógica para registrar la auditoría del reporte
    }
}

class Empleado {
    private String id;
    private String nombre;
    private double salario;

    public Empleado(String id, String nombre, double salario) {
        this.id = id;
        this.nombre = nombre;
        this.salario = salario;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSalario() {
        return salario;
    }
}