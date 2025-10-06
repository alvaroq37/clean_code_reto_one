// Función con olores de código que mezcla generación, notificación y
// persistencia de un reporte de empleados.  Úsala para identificar y
// refactorizar en el paso 5.

export interface Empleado {
  id: string;
  nombre: string;
  salario: number;
}

export function generarReporte(empleados: Empleado[]): string {
  let contenido = '';
  for (const e of empleados) {
    contenido += `${e.id};${e.nombre};${e.salario}\n`;
  }
  enviarPorCorreo(contenido);
  guardarEnArchivo(contenido);
  registrarAuditoria('REPORTE', contenido.length, new Date());
  return contenido;
}

function enviarPorCorreo(contenido: string): void {
  // Simular envío de correo
}

function guardarEnArchivo(contenido: string): void {
  // Simular persistencia
}

function registrarAuditoria(tipo: string, longitud: number, fecha: Date): void {
  // Simular registro de auditoría
}