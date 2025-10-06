/*Refactorización SOLID del Código
Análisis de Violaciones SOLID:
SRP: Una clase con 3 responsabilidades diferentes
OCP: Difícil extender con nuevos formatos o canales
DIP: Depende de implementaciones concretas en lugar de abstracciones

Código Refactorizado:
1. Interfaz del Reporte
*/
/**
 * Contrato para la generación de reportes en diferentes formatos
 */
public interface GeneradorReporte {
    byte[] generar();
    FormatoReporte getFormato();
}

public enum FormatoReporte {
    PDF, EXCEL, HTML, CSV
}
//2. Interfaz de Notificación
/**
 * Contrato para enviar reportes a través de diferentes canales
 */
public interface NotificadorReporte {
    void enviar(byte[] reporte, String destinatario);
    CanalNotificacion getCanal();
}

public enum CanalNotificacion {
    EMAIL, SLACK, SMS, WEBHOOK
}
//3. Interfaz de Persistencia
/**
 * Contrato para almacenar reportes en diferentes repositorios
 */
public interface RepositorioReporte {
    void guardar(byte[] reporte, String nombreArchivo);
    TipoRepositorio getTipo();
}

public enum TipoRepositorio {
    BASE_DATOS, S3, GOOGLE_DRIVE, LOCAL
}
/*Implementaciones Específicas:
4. Generadores de Reporte
*/
public class GeneradorPDF implements GeneradorReporte {
    private final DatosReporte datos;
    
    public GeneradorPDF(DatosReporte datos) {
        this.datos = datos;
    }
    
    @Override
    public byte[] generar() {
        // Lógica específica para generar PDF
        System.out.println("Generando reporte PDF...");
        return new byte[0]; // PDF generado
    }
    
    @Override
    public FormatoReporte getFormato() {
        return FormatoReporte.PDF;
    }
}

public class GeneradorExcel implements GeneradorReporte {
    private final DatosReporte datos;
    
    public GeneradorExcel(DatosReporte datos) {
        this.datos = datos;
    }
    
    @Override
    public byte[] generar() {
        // Lógica específica para generar Excel
        System.out.println("Generando reporte Excel...");
        return new byte[0]; // Excel generado
    }
    
    @Override
    public FormatoReporte getFormato() {
        return FormatoReporte.EXCEL;
    }
}
//5. Notificadores
public class NotificadorEmail implements NotificadorReporte {
    private final ServicioEmail servicioEmail;
    
    public NotificadorEmail(ServicioEmail servicioEmail) {
        this.servicioEmail = servicioEmail;
    }
    
    @Override
    public void enviar(byte[] reporte, String destinatario) {
        // Lógica específica para enviar email
        System.out.println("Enviando reporte por email a: " + destinatario);
        servicioEmail.enviarConAdjunto(destinatario, "Reporte", reporte);
    }
    
    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.EMAIL;
    }
}

public class NotificadorSlack implements NotificadorReporte {
    private final ClienteSlack clienteSlack;
    
    public NotificadorSlack(ClienteSlack clienteSlack) {
        this.clienteSlack = clienteSlack;
    }
    
    @Override
    public void enviar(byte[] reporte, String canalDestino) {
        // Lógica específica para enviar a Slack
        System.out.println("Enviando reporte a canal Slack: " + canalDestino);
        clienteSlack.enviarArchivo(canalDestino, reporte, "reporte.pdf");
    }
    
    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.SLACK;
    }
}
//6. Repositorios
public class RepositorioBaseDatos implements RepositorioReporte {
    private final DataSource dataSource;
    
    public RepositorioBaseDatos(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void guardar(byte[] reporte, String nombreArchivo) {
        // Lógica específica para guardar en base de datos
        System.out.println("Guardando reporte en base de datos: " + nombreArchivo);
        // Implementación con JDBC/JPA
    }
    
    @Override
    public TipoRepositorio getTipo() {
        return TipoRepositorio.BASE_DATOS;
    }
}

public class RepositorioS3 implements RepositorioReporte {
    private final ClienteS3 clienteS3;
    private final String bucketName;
    
    public RepositorioS3(ClienteS3 clienteS3, String bucketName) {
        this.clienteS3 = clienteS3;
        this.bucketName = bucketName;
    }
    
    @Override
    public void guardar(byte[] reporte, String nombreArchivo) {
        // Lógica específica para guardar en S3
        System.out.println("Guardando reporte en S3: " + nombreArchivo);
        clienteS3.subirArchivo(bucketName, nombreArchivo, reporte);
    }
    
    @Override
    public TipoRepositorio getTipo() {
        return TipoRepositorio.S3;
    }
}
/*Servicio de Coordinación:
7. Servicio Principal de Reportes
/**
 * Servicio que coordina la generación, notificación y persistencia de reportes
 * Aplica el principio de inversión de dependencias (DIP)
 */
public class ServicioReportes {
    private final GeneradorReporte generador;
    private final List<NotificadorReporte> notificadores;
    private final List<RepositorioReporte> repositorios;
    
    public ServicioReportes(GeneradorReporte generador, 
                          List<NotificadorReporte> notificadores,
                          List<RepositorioReporte> repositorios) {
        this.generador = generador;
        this.notificadores = notificadores;
        this.repositorios = repositorios;
    }
    
    public ResultadoReporte procesarReporte(SolicitudReporte solicitud) {
        try {
            // 1. Generar reporte
            byte[] reporte = generador.generar();
            
            // 2. Guardar en repositorios
            String nombreArchivo = generarNombreArchivo(solicitud);
            for (RepositorioReporte repositorio : repositorios) {
                repositorio.guardar(reporte, nombreArchivo);
            }
            
            // 3. Notificar a destinatarios
            for (String destinatario : solicitud.getDestinatarios()) {
                for (NotificadorReporte notificador : notificadores) {
                    notificador.enviar(reporte, destinatario);
                }
            }
            
            return ResultadoReporte.exitoso(nombreArchivo, reporte.length);
            
        } catch (Exception e) {
            return ResultadoReporte.fallido(e.getMessage());
        }
    }
    
    private String generarNombreArchivo(SolicitudReporte solicitud) {
        return String.format("reporte_%s_%tF.%s", 
                           solicitud.getTipo(),
                           solicitud.getFecha(),
                           generador.getFormato().name().toLowerCase());
    }
}
//8. Objetos de Datos
public class SolicitudReporte {
    private final String tipo;
    private final LocalDate fecha;
    private final List<String> destinatarios;
    
    public SolicitudReporte(String tipo, LocalDate fecha, List<String> destinatarios) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.destinatarios = destinatarios;
    }
    
    // Getters...
}

public class ResultadoReporte {
    private final boolean exito;
    private final String mensaje;
    private final String nombreArchivo;
    private final int tamañoBytes;
    
    private ResultadoReporte(boolean exito, String mensaje, String nombreArchivo, int tamañoBytes) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nombreArchivo = nombreArchivo;
        this.tamañoBytes = tamañoBytes;
    }
    
    public static ResultadoReporte exitoso(String nombreArchivo, int tamañoBytes) {
        return new ResultadoReporte(true, "Reporte procesado exitosamente", nombreArchivo, tamañoBytes);
    }
    
    public static ResultadoReporte fallido(String mensajeError) {
        return new ResultadoReporte(false, mensajeError, null, 0);
    }
    
    // Getters...
}

public class DatosReporte {
    private final Map<String, Object> datos;
    private final String titulo;
    private final LocalDate periodo;
    
    public DatosReporte(String titulo, LocalDate periodo, Map<String, Object> datos) {
        this.titulo = titulo;
        this.periodo = periodo;
        this.datos = datos;
    }
    
    // Getters...
}
/*Factory para Creación Flexible:
9. Factories
*/
public class FactoryReportes {
    public static GeneradorReporte crearGenerador(FormatoReporte formato, DatosReporte datos) {
        switch (formato) {
            case PDF:
                return new GeneradorPDF(datos);
            case EXCEL:
                return new GeneradorExcel(datos);
            case HTML:
                return new GeneradorHTML(datos);
            case CSV:
                return new GeneradorCSV(datos);
            default:
                throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }
    
    public static NotificadorReporte crearNotificador(CanalNotificacion canal, ConfiguracionNotificador config) {
        switch (canal) {
            case EMAIL:
                return new NotificadorEmail(config.getServicioEmail());
            case SLACK:
                return new NotificadorSlack(config.getClienteSlack());
            case SMS:
                return new NotificadorSMS(config.getProveedorSMS());
            default:
                throw new IllegalArgumentException("Canal no soportado: " + canal);
        }
    }
}

//Ejemplo de Uso:
public class EjemploUsoReportes {
    public static void main(String[] args) {
        // Configuración
        DatosReporte datos = new DatosReporte("Ventas Mensuales", LocalDate.now(), obtenerDatosVentas());
        
        // Crear componentes específicos
        GeneradorReporte generador = FactoryReportes.crearGenerador(FormatoReporte.PDF, datos);
        
        List<NotificadorReporte> notificadores = Arrays.asList(
            new NotificadorEmail(new ServicioEmail()),
            new NotificadorSlack(new ClienteSlack())
        );
        
        List<RepositorioReporte> repositorios = Arrays.asList(
            new RepositorioBaseDatos(dataSource),
            new RepositorioS3(clienteS3, "mis-reportes")
        );
        
        // Servicio principal
        ServicioReportes servicio = new ServicioReportes(generador, notificadores, repositorios);
        
        // Procesar reporte
        SolicitudReporte solicitud = new SolicitudReporte(
            "ventas", 
            LocalDate.now(), 
            Arrays.asList("gerente@empresa.com", "canal-ventas")
        );
        
        ResultadoReporte resultado = servicio.procesarReporte(solicitud);
        
        if (resultado.isExito()) {
            System.out.println("Reporte generado: " + resultado.getNombreArchivo());
        } else {
            System.out.println("Error: " + resultado.getMensaje());
        }
    }
}