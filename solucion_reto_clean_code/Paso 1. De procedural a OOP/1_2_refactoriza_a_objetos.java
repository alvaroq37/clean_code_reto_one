import java.util.ArrayList;
import java.util.List;

// 1. PRINCIPIO DE RESPONSABILIDAD ÚNICA (SRP)
interface OperacionBancaria {
    void ejecutar() throws OperacionBancariaException;
    String obtenerDescripcion();
}

// 2. EXCEPCIONES ESPECÍFICAS DEL DOMINIO
class OperacionBancariaException extends Exception {
    public OperacionBancariaException(String mensaje) {
        super(mensaje);
    }
}

class SaldoInsuficienteException extends OperacionBancariaException {
    public SaldoInsuficienteException(double monto, double saldoDisponible) {
        super(String.format("Saldo insuficiente. Monto: %.2f, Saldo disponible: %.2f", 
                          monto, saldoDisponible));
    }
}

class MontoInvalidoException extends OperacionBancariaException {
    public MontoInvalidoException(double monto) {
        super(String.format("Monto inválido: %.2f", monto));
    }
}

// 3. ENCAPSULAMIENTO Y VALIDACIONES
abstract class CuentaBancaria {
    private final String numeroCuenta;
    private final String titular;
    private double saldo;
    private final List<String> historial;

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.historial = new ArrayList<>();
        registrarEnHistorial("Cuenta creada con saldo inicial: " + saldoInicial);
    }

    // 4. MÉTODOS PROTEGIDOS PARA HERENCIA
    protected final void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException(monto);
        }
    }

    protected final void validarSaldoSuficiente(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException(monto, saldo);
        }
    }

    protected final void registrarEnHistorial(String operacion) {
        historial.add(String.format("[%s] %s", java.time.LocalDateTime.now(), operacion));
    }

    // 5. OPERACIONES BANCARIAS COMO OBJETOS
    public class Retiro implements OperacionBancaria {
        private final double monto;

        public Retiro(double monto) {
            this.monto = monto;
        }

        @Override
        public void ejecutar() throws OperacionBancariaException {
            validarMonto(monto);
            validarSaldoSuficiente(monto);
            saldo -= monto;
            registrarEnHistorial(String.format("Retiro: -%.2f", monto));
        }

        @Override
        public String obtenerDescripcion() {
            return String.format("Retiro de %.2f de la cuenta %s", monto, numeroCuenta);
        }
    }

    public class Deposito implements OperacionBancaria {
        private final double monto;

        public Deposito(double monto) {
            this.monto = monto;
        }

        @Override
        public void ejecutar() throws OperacionBancariaException {
            validarMonto(monto);
            saldo += monto;
            registrarEnHistorial(String.format("Depósito: +%.2f", monto));
        }

        @Override
        public String obtenerDescripcion() {
            return String.format("Depósito de %.2f en la cuenta %s", monto, numeroCuenta);
        }
    }

    // 6. MÉTODOS PÚBLICOS QUE DEVUELVEN OPERACIONES
    public OperacionBancaria crearRetiro(double monto) {
        return new Retiro(monto);
    }

    public OperacionBancaria crearDeposito(double monto) {
        return new Deposito(monto);
    }

    // 7. GETTERS CON INMUTABILIDAD
    public final String getNumeroCuenta() { return numeroCuenta; }
    public final String getTitular() { return titular; }
    public final double getSaldo() { return saldo; }
    public final List<String> getHistorial() { return new ArrayList<>(historial); }

    @Override
    public String toString() {
        return String.format("Cuenta %s - Titular: %s - Saldo: %.2f", 
                           numeroCuenta, titular, saldo);
    }
}

// 8. COMPOSICIÓN SOBRE HERENCIA - TRANSFERENCIA COMO OPERACIÓN COMPUESTA
class Transferencia implements OperacionBancaria {
    private final CuentaBancaria origen;
    private final CuentaBancaria destino;
    private final double monto;

    public Transferencia(CuentaBancaria origen, CuentaBancaria destino, double monto) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
    }

    @Override
    public void ejecutar() throws OperacionBancariaException {
        // 9. PATRÓN: Unit of Work implícito - ambas operaciones se ejecutan atómicamente
        OperacionBancaria retiro = origen.crearRetiro(monto);
        OperacionBancaria deposito = destino.crearDeposito(monto);

        retiro.ejecutar();
        try {
            deposito.ejecutar();
        } catch (OperacionBancariaException e) {
            // 10. COMPENSACIÓN en caso de error
            origen.crearDeposito(monto).ejecutar();
            throw new OperacionBancariaException("Transferencia fallida: " + e.getMessage());
        }
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("Transferencia de %.2f de %s a %s", 
                           monto, origen.getTitular(), destino.getTitular());
    }
}

// 11. SERVICIO BANCARIO QUE ORQUESTA OPERACIONES
class ServicioBancario {
    public void procesarOperacion(OperacionBancaria operacion) {
        try {
            operacion.ejecutar();
            System.out.println("✓ " + operacion.obtenerDescripcion());
        } catch (OperacionBancariaException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void mostrarEstadoCuentas(CuentaBancaria... cuentas) {
        System.out.println("\n--- ESTADO DE CUENTAS ---");
        for (CuentaBancaria cuenta : cuentas) {
            System.out.println(cuenta);
        }
        System.out.println("-------------------------\n");
    }
}

// 12. CLASE PRINCIPAL DEMOSTRATIVA
public class Step1OOP {
    public static void main(String[] args) {
        // Creación de cuentas
        CuentaBancaria cuentaA = new CuentaBancaria("001", "Ana García", 1000) {};
        CuentaBancaria cuentaB = new CuentaBancaria("002", "Carlos López", 500) {};

        ServicioBancario servicio = new ServicioBancario();

        // Mostrar estado inicial
        servicio.mostrarEstadoCuentas(cuentaA, cuentaB);

        // Operaciones individuales
        servicio.procesarOperacion(cuentaA.crearRetiro(200));
        servicio.procesarOperacion(cuentaB.crearDeposito(100));

        // Transferencia exitosa
        servicio.procesarOperacion(new Transferencia(cuentaA, cuentaB, 300));

        // Transferencia fallida (saldo insuficiente)
        servicio.procesarOperacion(new Transferencia(cuentaA, cuentaB, 2000));

        // Operación inválida
        servicio.procesarOperacion(cuentaA.crearRetiro(-50));

        // Estado final
        servicio.mostrarEstadoCuentas(cuentaA, cuentaB);

        // Mostrar historial
        System.out.println("--- HISTORIAL CUENTA A ---");
        cuentaA.getHistorial().forEach(System.out::println);
    }
}