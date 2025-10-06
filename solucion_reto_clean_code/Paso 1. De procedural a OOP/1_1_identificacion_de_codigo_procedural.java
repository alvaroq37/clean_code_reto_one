/*Identificación de Código Procedimental
Características Procedimentales Identificadas:
1. Variables Globales Estáticas
*/
static double saldoCuentaA = 1000;
static double saldoCuentaB = 500;
Problema: Estado global compartido que puede ser modificado desde cualquier parte del programa sin control.

//2. Función con Lógica de Control Compleja
public static void transferir(String origen, String destino, double monto) {
    if ("A".equals(origen)) {
        if (saldoCuentaA >= monto) {
            saldoCuentaA -= monto;
            saldoCuentaB += monto;
            System.out.println("Transferencia exitosa.");
        } else {
            System.out.println("Saldo insuficiente.");
        }
    } else if ("B".equals(origen)) {
        // Lógica duplicada...
    }
}
/*3. Mezcla de Responsabilidades
La función transferir realiza múltiples tareas:
	- Validación de saldo
	- Actualización de estado
	- Lógica de negocio
	- Presentación (mensajes al usuario)
	- Control de flujo complejo

4. Acoplamiento con Entrada/Salida
*/
System.out.println("Transferencia exitosa.");  // Mezcla lógica con UI
System.out.println("Saldo insuficiente.");     // Presentación en lógica de negocio
//5. Lógica Duplicada
// Misma lógica repetida para cada cuenta
if ("A".equals(origen)) {
    if (saldoCuentaA >= monto) {
        saldoCuentaA -= monto;
        saldoCuentaB += monto;
        // ...
    }
} else if ("B".equals(origen)) {
    if (saldoCuentaB >= monto) {
        saldoCuentaB -= monto;
        saldoCuentaA += monto;
        // ...
    }
}
/*6. Falta de Encapsulación
No hay objetos que encapsulen el estado y comportamiento:

Los saldos son variables públicas

No hay métodos de acceso controlado

No hay validaciones encapsuladas*/