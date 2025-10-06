// Refactorización de la clase Informe aplicando principios SOLID.  Se
// definen interfaces y clases específicas para cada responsabilidad, y se
// utilizan en un servicio que orquesta el flujo.

// Interfaces
export interface GeneradorInforme {
  generar(data: any): void;
}

export interface Notificador {
  notificar(data: any): void;
}

export interface Repositorio {
  guardar(data: any): void;
}

// Implementaciones concretas
export class InformePdf implements GeneradorInforme {
  generar(data: any): void {
    // Implementar generación de PDF
  }
}

export class EmailNotificador implements Notificador {
  notificar(data: any): void {
    // Implementar notificación por correo
  }
}

export class InformeRepositorio implements Repositorio {
  guardar(data: any): void {
    // Implementar persistencia del informe
  }
}

// Servicio que coordina el proceso
export class InformeService {
  constructor(
    private generador: GeneradorInforme,
    private notificador: Notificador,
    private repositorio: Repositorio
  ) {}

  procesar(data: any): void {
    this.generador.generar(data);
    this.notificador.notificar(data);
    this.repositorio.guardar(data);
  }
}