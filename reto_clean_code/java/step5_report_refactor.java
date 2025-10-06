import java.util.Date;
import java.util.List;

/**
 * Refactorización del reporte de empleados.  Se separan las responsabilidades
 * en clases distintas, reduciendo la longitud de métodos y favoreciendo la
 * reutilización.  Además, se aplican los principios KISS y DRY.
 */
public class ReporteEmpleadosRefactor {
    private final FormateadorReporte formateador;
    private final NotificadorReporte notificador;
    private final PersistenciaReporte persistencia;
    private final Auditor auditor;

    public ReporteEmpleadosRefactor(FormateadorReporte formateador,
                                    NotificadorReporte notificador,
                                    PersistenciaReporte persistencia,
                                    Auditor auditor) {
        this.formateador = formateador;
        this.notificador = notificador;
        this.persistencia = persistencia;
        this.auditor = auditor;
    }

    public String generarReporte(List<Empleado> empleados) {
        String contenido = formateador.formatear(empleados);
        notificador.enviar(contenido);
        persistencia.guardar(contenido);
        auditor.registrar("REPORTE", contenido.length(), new Date());
        return contenido;
    }
}

interface FormateadorReporte {
    String formatear(List<Empleado> empleados);
}

class FormateadorCSV implements FormateadorReporte {
    @Override
    public String formatear(List<Empleado> empleados) {
        StringBuilder sb = new StringBuilder();
        for (Empleado e : empleados) {
            sb.append(e.getId()).append(";")
              .append(e.getNombre()).append(";")
              .append(e.getSalario()).append("\n");
        }
        return sb.toString();
    }
}

interface NotificadorReporte {
    void enviar(String contenido);
}

class EmailNotificadorReporte implements NotificadorReporte {
    @Override
    public void enviar(String contenido) {
        // Implementar envío de correo
    }
}

interface PersistenciaReporte {
    void guardar(String contenido);
}

class ArchivoPersistenciaReporte implements PersistenciaReporte {
    @Override
    public void guardar(String contenido) {
        // Implementar guardado en archivo o base de datos
    }
}

interface Auditor {
    void registrar(String tipo, int longitud, Date fecha);
}

class AuditorSimple implements Auditor {
    @Override
    public void registrar(String tipo, int longitud, Date fecha) {
        // Implementar registro de auditoría
    }
}