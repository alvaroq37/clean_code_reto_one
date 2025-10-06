/*Refactorización aplicando KISS, DRY y YAGNI
Análisis de Principios:
KISS (Keep It Simple, Stupid)
Eliminar complejidad innecesaria

Métodos pequeños y enfocados

DRY (Don't Repeat Yourself)
Evitar duplicación de lógica

Reutilizar código común

YAGNI (You Aren't Gonna Need It)
Solo implementar lo necesario

Evitar funcionalidades "por si acaso"

Código Refactorizado:
1. Clases de Dominio (Sin Cambios - Ya son simples)
*/
class Empleado {
    private String id;
    private String nombre;
    private double salario;

    public Empleado(String id, String nombre, double salario) {
        this.id = id;
        this.nombre = nombre;
        this.salario = salario;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getSalario() { return salario; }
}

//2. Generador de Reporte (Responsabilidad Única)
public class GeneradorReporte {
    
    public String generarCSV(List<Empleado> empleados) {
        StringBuilder reporte = new StringBuilder();
        
        for (Empleado empleado : empleados) {
            agregarFila(reporte, empleado);
        }
        
        return reporte.toString();
    }
    
    private void agregarFila(StringBuilder reporte, Empleado empleado) {
        reporte.append(empleado.getId())
               .append(";")
               .append(empleado.getNombre())
               .append(";")
               .append(empleado.getSalario())
               .append("\n");
    }
}

//3. Servicio de Notificación (Simple y Enfocado)
public class NotificadorEmail {
    
    public void enviarReporte(String contenidoReporte, String destinatario) {
        // Lógica simple para enviar email
        System.out.println("Enviando reporte a: " + destinatario);
        System.out.println("Tamaño del reporte: " + contenidoReporte.length() + " caracteres");
        // Implementación real iría aquí
    }
}

//4. Servicio de Archivos (KISS)
public class GestorArchivos {
    
    public void guardarReporte(String contenido, String nombreArchivo) {
        // Lógica simple para guardar archivo
        System.out.println("Guardando reporte en: " + nombreArchivo);
        System.out.println("Contenido: " + contenido.length() + " caracteres");
        // Implementación real iría aquí
    }
}

//5. Servicio de Auditoría (DRY - Sin duplicación)
public class Auditor {
    
    public void registrarGeneracionReporte(String tipo, int tamaño) {
        String registro = String.format(
            "AUDITORIA - %s: %d bytes - %s",
            tipo, tamaño, java.time.LocalDateTime.now()
        );
        System.out.println(registro);
    }
}

//6. Coordinador Principal (Orquestación Simple)
public class ProcesadorReportes {
    private final GeneradorReporte generador;
    private final NotificadorEmail notificador;
    private final GestorArchivos gestorArchivos;
    private final Auditor auditor;
    
    // Constructor simple - Inyección de dependencias básica
    public ProcesadorReportes() {
        this.generador = new GeneradorReporte();
        this.notificador = new NotificadorEmail();
        this.gestorArchivos = new GestorArchivos();
        this.auditor = new Auditor();
    }
    
    // Método principal - KISS: Una responsabilidad clara
    public void procesarReporteEmpleados(List<Empleado> empleados, 
                                       String destinatarioEmail) {
        // 1. Generar reporte
        String reporte = generador.generarCSV(empleados);
        
        // 2. Guardar archivo
        String nombreArchivo = "reporte_empleados.csv";
        gestorArchivos.guardarReporte(reporte, nombreArchivo);
        
        // 3. Enviar notificación
        notificador.enviarReporte(reporte, destinatarioEmail);
        
        // 4. Registrar auditoría
        auditor.registrarGeneracionReporte("REPORTE_EMPLEADOS", reporte.length());
    }
}

//7. Clase de Ejemplo de Uso
public class Main {
    public static void main(String[] args) {
        // Datos de ejemplo
        List<Empleado> empleados = Arrays.asList(
            new Empleado("001", "Ana García", 45000.0),
            new Empleado("002", "Carlos López", 52000.0),
            new Empleado("003", "María Rodríguez", 48000.0)
        );
        
        // Procesar reporte - Simple y directo
        ProcesadorReportes procesador = new ProcesadorReportes();
        procesador.procesarReporteEmpleados(empleados, "gerencia@empresa.com");
    }
}
