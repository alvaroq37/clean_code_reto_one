/*Análisis de Violaciones en el Código
Violaciones Detectadas:
1.Principio de Responsabilidad Única (SRP) - VIOLADO
Problema: La clase Informe tiene múltiples responsabilidades no relacionadas
*/
public class Informe {
    public void generarPDF() {        // Responsabilidad 1: Generación de formato
    public void enviarEmail() {       // Responsabilidad 2: Notificación
    public void guardarEnBaseDatos() { // Responsabilidad 3: Persistencia
}
/*Cada método representa un motivo diferente para cambiar:
generarPDF() cambia si se modifica el formato del PDF
enviarEmail() cambia si se actualiza el protocolo de email
guardarEnBaseDatos() cambia si se migra la base de datos

2. Alta Cohesión - VIOLADO
Problema: La clase realiza tareas que no están relacionadas conceptualmente:
	Generación de documentos
	Comunicación por email
	Operaciones de base de datos

Estas son preocupaciones separadas que deberían estar en clases diferentes.

3. Bajo Acoplamiento - VIOLADO
	Problema: La clase está acoplada a:
	Librerías específicas de generación de PDF
	Protocolos de envío de email
	Tecnología específica de base de datos
	Cualquier cambio en estas dependencias requiere modificar la misma clase.

4. Principio Abierto/Cerrado (OCP) - VIOLADO
Problema: Difícil extender con nueva funcionalidad
*/
// Para añadir nuevo formato, hay que modificar la clase
public void generarExcel() { ... }  // Requiere modificar clase existente

// Para añadir nuevo canal, hay que modificar la clase  
public void enviarSlack() { ... }   // Requiere modificar clase existente

/*5. Separación de Concerns - VIOLADO
Problema: Mezcla diferentes capas de la aplicación:
	Lógica de negocio (generación de informe)
	Infraestructura (base de datos, email)
	Presentación (formato PDF)
*/
