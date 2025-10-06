// Refactorización del reporte de empleados aplicando KISS y DRY.  Se separan
// responsabilidades y se introducen clases e interfaces para la generación,
// notificación y persistencia.

export interface Empleado {
  id: string;
  nombre: string;
  salario: number;
}

export interface FormateadorReporte {
  formatear(empleados: Empleado[]): string;
}

export class FormateadorCSV implements FormateadorReporte {
  formatear(empleados: Empleado[]): string {
    return empleados.map(e => `${e.id};${e.nombre};${e.salario}\n`).join('');
  }
}

export interface NotificadorReporte {
  enviar(contenido: string): void;
}

export class EmailNotificadorReporte implements NotificadorReporte {
  enviar(contenido: string): void {
    // Simular envío de correo
  }
}

export interface PersistenciaReporte {
  guardar(contenido: string): void;
}

export class ArchivoPersistenciaReporte implements PersistenciaReporte {
  guardar(contenido: string): void {
    // Simular persistencia
  }
}

export interface Auditor {
  registrar(tipo: string, longitud: number, fecha: Date): void;
}

export class AuditorSimple implements Auditor {
  registrar(tipo: string, longitud: number, fecha: Date): void {
    // Simular registro de auditoría
  }
}

export class ReporteEmpleadosService {
  constructor(
    private formateador: FormateadorReporte,
    private notificador: NotificadorReporte,
    private persistencia: PersistenciaReporte,
    private auditor: Auditor
  ) {}

  generarReporte(empleados: Empleado[]): string {
    const contenido = this.formateador.formatear(empleados);
    this.notificador.enviar(contenido);
    this.persistencia.guardar(contenido);
    this.auditor.registrar('REPORTE', contenido.length, new Date());
    return contenido;
  }
}