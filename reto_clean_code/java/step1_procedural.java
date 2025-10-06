public class Step1Procedural {
    static double saldoCuentaA = 1000;
    static double saldoCuentaB = 500;

    /**
     * Función procedural para transferir fondos entre dos cuentas.  No utiliza
     * encapsulación ni orientación a objetos, por lo que manipula variables
     * globales directamente y mezcla la lógica de negocio con mensajes al
     * usuario.
     */
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
            if (saldoCuentaB >= monto) {
                saldoCuentaB -= monto;
                saldoCuentaA += monto;
                System.out.println("Transferencia exitosa.");
            } else {
                System.out.println("Saldo insuficiente.");
            }
        }
    }

    public static void main(String[] args) {
        transferir("A", "B", 200);
        System.out.println("Saldo A: " + saldoCuentaA + ", Saldo B: " + saldoCuentaB);
    }
}