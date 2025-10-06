/*Análisis de Olores de Código en ReporteEmpleados
Identificación de Problemas:

1. Violación del Principio de Responsabilidad Única (SRP)
La clase ReporteEmpleados tiene 4 responsabilidades diferentes:

Generación del reporte (formateo de datos)

Notificación (enviar por correo)

Persistencia (guardar en archivo)

Auditoría (registro de actividades)

2. Método Demasiado Largo y Complejo
El método generarReporte() realiza múltiples tareas no relacionadas:
*/
public String generarReporte(List<Empleado> empleados) {
    // 1. Generar contenido
    // 2. Enviar correo
    // 3. Guardar archivo  
    // 4. Registrar auditoría
    // 5. Retornar resultado
}
/*
3. Acoplamiento Excesivo
La clase está fuertemente acoplada a:
- Formato específico de reporte (CSV con ;)
- Mecanismos de envío (email)
- Sistema de archivos
- Sistema de auditoría

4. Falta de Abstracción
No hay interfaces para:
- Diferentes formatos de reporte
- Múltiples canales de notificación
- Varios sistemas de persistencia

5. Dificultad para Testear

Es difícil testear unitariamente porque:
- Mezcla lógica de negocio con I/O
- Tiene efectos secundarios múltiples
- No se puede mockear fácilmente
*/