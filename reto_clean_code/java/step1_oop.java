/**
 * Ejemplo de refactorización orientada a objetos para una operación de
 * transferencia bancaria.  Se crean clases para encapsular el estado y el
 * comportamiento de las cuentas y de la operación de transferencia,
 * facilitando así la mantenibilidad y la extensibilidad.
 */
class CuentaBancaria {
    private String titular;
    private double saldo;

    public CuentaBancaria(String titular, double saldoInicial) {
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public boolean retirar(double monto) {
        if (monto <= saldo) {
            saldo -= monto;
            return true;
        }
        return false;
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return titular;
    }
}

class Transferencia {
    public void realizar(CuentaBancaria origen, CuentaBancaria destino, double monto) {
        if (origen.retirar(monto)) {
            destino.depositar(monto);
            System.out.println("Transferencia de " + monto + " de " + origen.getTitular() + " a " + destino.getTitular());
        } else {
            System.out.println("Saldo insuficiente");
        }
    }
}

public class Step1OOP {
    public static void main(String[] args) {
        CuentaBancaria cuentaA = new CuentaBancaria("A", 1000);
        CuentaBancaria cuentaB = new CuentaBancaria("B", 500);
        Transferencia transferencia = new Transferencia();
        transferencia.realizar(cuentaA, cuentaB, 200);
        System.out.println("Saldo A: " + cuentaA.getSaldo() + ", Saldo B: " + cuentaB.getSaldo());
    }
}