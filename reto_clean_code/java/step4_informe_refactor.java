/**
 * Refactorización de la clase Informe para cumplir con los principios SOLID.  Se
 * separan las responsabilidades en varias interfaces e implementaciones y se
 * inyectan como dependencias en un servicio que coordina el proceso.
 */

interface GeneradorInforme {
    void generar(InformeData data);
}

class InformePDF implements GeneradorInforme {
    @Override
    public void generar(InformeData data) {
        // Implementar generación de PDF
    }
}

interface Notificador {
    void notificar(InformeData data);
}

class EmailNotificador implements Notificador {
    @Override
    public void notificar(InformeData data) {
        // Implementar notificación por correo electrónico
    }
}

interface Repositorio {
    void guardar(InformeData data);
}

class InformeRepositorio implements Repositorio {
    @Override
    public void guardar(InformeData data) {
        // Implementar persistencia del informe en una base de datos
    }
}

class InformeService {
    private GeneradorInforme generador;
    private Notificador notificador;
    private Repositorio repositorio;

    public InformeService(GeneradorInforme generador, Notificador notificador, Repositorio repositorio) {
        this.generador = generador;
        this.notificador = notificador;
        this.repositorio = repositorio;
    }

    public void procesar(InformeData data) {
        generador.generar(data);
        notificador.notificar(data);
        repositorio.guardar(data);
    }
}

class InformeData {
    // Campos del informe (placeholder)
}