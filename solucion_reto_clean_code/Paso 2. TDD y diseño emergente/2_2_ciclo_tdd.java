/*
Paso 1: RED - Escribir pruebas que fallen
Primero, escribamos las pruebas que definan el comportamiento esperado
*/
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Step1ProceduralTest {
    
    @Test
    public void testTransferenciaExitosaDeAaB() {
        // Given
        Step1Procedural.saldoCuentaA = 1000;
        Step1Procedural.saldoCuentaB = 500;
        
        // When
        Step1Procedural.transferir("A", "B", 200);
        
        // Then
        assertEquals(800, Step1Procedural.saldoCuentaA, 0.001);
        assertEquals(700, Step1Procedural.saldoCuentaB, 0.001);
    }
    
    @Test
    public void testTransferenciaExitosaDeBaA() {
        // Given
        Step1Procedural.saldoCuentaA = 1000;
        Step1Procedural.saldoCuentaB = 500;
        
        // When
        Step1Procedural.transferir("B", "A", 100);
        
        // Then
        assertEquals(1100, Step1Procedural.saldoCuentaA, 0.001);
        assertEquals(400, Step1Procedural.saldoCuentaB, 0.001);
    }
    
    @Test
    public void testTransferenciaConSaldoInsuficiente() {
        // Given
        Step1Procedural.saldoCuentaA = 100;
        Step1Procedural.saldoCuentaB = 500;
        
        // When
        Step1Procedural.transferir("A", "B", 200);
        
        // Then - Los saldos no deben cambiar
        assertEquals(100, Step1Procedural.saldoCuentaA, 0.001);
        assertEquals(500, Step1Procedural.saldoCuentaB, 0.001);
    }
    
    @Test
    public void testTransferenciaConMontoCero() {
        // Given
        Step1Procedural.saldoCuentaA = 1000;
        Step1Procedural.saldoCuentaB = 500;
        
        // When
        Step1Procedural.transferir("A", "B", 0);
        
        // Then - Los saldos no deben cambiar
        assertEquals(1000, Step1Procedural.saldoCuentaA, 0.001);
        assertEquals(500, Step1Procedural.saldoCuentaB, 0.001);
    }
    
    @Test
    public void testTransferenciaConMontoNegativo() {
        // Given
        Step1Procedural.saldoCuentaA = 1000;
        Step1Procedural.saldoCuentaB = 500;
        
        // When
        Step1Procedural.transferir("A", "B", -50);
        
        // Then - Los saldos no deben cambiar
        assertEquals(1000, Step1Procedural.saldoCuentaA, 0.001);
        assertEquals(500, Step1Procedural.saldoCuentaB, 0.001);
    }
}


/*
Paso 2: GREEN - Implementar mínima lógica para que pasen las pruebas
Ahora refactoricemos el código para que pase todas las pruebas
*/
import java.util.HashMap;
import java.util.Map;

public class Step1Procedural {
    // Cambiamos a mapa para mayor flexibilidad
    static Map<String, Double> saldos = new HashMap<>();
    
    static {
        // Inicializamos las cuentas
        saldos.put("A", 1000.0);
        saldos.put("B", 500.0);
    }
    
    /**
     * Función mejorada que utiliza mapa para manejar múltiples cuentas
     */
    public static void transferir(String origen, String destino, double monto) {
        // Validaciones básicas
        if (monto <= 0) {
            System.out.println("Monto debe ser positivo.");
            return;
        }
        
        if (!saldos.containsKey(origen) || !saldos.containsKey(destino)) {
            System.out.println("Cuenta no existe.");
            return;
        }
        
        // Verificar saldo suficiente
        if (saldos.get(origen) >= monto) {
            // Realizar transferencia
            saldos.put(origen, saldos.get(origen) - monto);
            saldos.put(destino, saldos.get(destino) + monto);
            System.out.println("Transferencia exitosa de " + monto + 
                             " de " + origen + " a " + destino);
        } else {
            System.out.println("Saldo insuficiente en cuenta " + origen);
        }
    }
    
    // Método auxiliar para pruebas
    public static double getSaldo(String cuenta) {
        return saldos.getOrDefault(cuenta, 0.0);
    }
    
    // Método para resetear saldos en pruebas
    public static void resetSaldos() {
        saldos.put("A", 1000.0);
        saldos.put("B", 500.0);
    }
    
    public static void main(String[] args) {
        transferir("A", "B", 200);
        System.out.println("Saldo A: " + getSaldo("A") + ", Saldo B: " + getSaldo("B"));
    }
}


/*
Paso 3: REFACTOR - Mejorar el código usando colecciones y principios sólidos
Ahora refactoricemos para hacer el código más mantenible y extensible
*/
import java.util.HashMap;
import java.util.Map;

public class SistemaBancario {
    private final Map<String, Double> cuentas;
    private final Map<String, String> titulares;
    
    public SistemaBancario() {
        this.cuentas = new HashMap<>();
        this.titulares = new HashMap<>();
        inicializarCuentasPorDefecto();
    }
    
    private void inicializarCuentasPorDefecto() {
        // Podemos cargar esto desde configuración en el futuro
        Map<String, Double> cuentasIniciales = Map.of(
            "A", 1000.0,
            "B", 500.0,
            "C", 2000.0  // Nueva cuenta para demostrar escalabilidad
        );
        
        Map<String, String> titularesIniciales = Map.of(
            "A", "Ana García",
            "B", "Carlos López", 
            "C", "Maria Rodriguez"
        );
        
        cuentas.putAll(cuentasIniciales);
        titulares.putAll(titularesIniciales);
    }
    
    /**
     * Transferencia entre cuentas con validaciones completas
     */
    public ResultadoTransferencia transferir(String cuentaOrigen, String cuentaDestino, double monto) {
        // Validaciones
        if (monto <= 0) {
            return new ResultadoTransferencia(false, "El monto debe ser positivo");
        }
        
        if (!cuentaExiste(cuentaOrigen)) {
            return new ResultadoTransferencia(false, "Cuenta origen no existe: " + cuentaOrigen);
        }
        
        if (!cuentaExiste(cuentaDestino)) {
            return new ResultadoTransferencia(false, "Cuenta destino no existe: " + cuentaDestino);
        }
        
        if (cuentaOrigen.equals(cuentaDestino)) {
            return new ResultadoTransferencia(false, "No se puede transferir a la misma cuenta");
        }
        
        // Verificar saldo
        if (obtenerSaldo(cuentaOrigen) < monto) {
            return new ResultadoTransferencia(false, 
                String.format("Saldo insuficiente. Disponible: %.2f, Solicitado: %.2f", 
                            obtenerSaldo(cuentaOrigen), monto));
        }
        
        // Ejecutar transferencia atómica
        cuentas.put(cuentaOrigen, obtenerSaldo(cuentaOrigen) - monto);
        cuentas.put(cuentaDestino, obtenerSaldo(cuentaDestino) + monto);
        
        return new ResultadoTransferencia(true, 
            String.format("Transferencia exitosa: %.2f de %s a %s", 
                         monto, cuentaOrigen, cuentaDestino));
    }
    
    // Métodos de consulta
    public boolean cuentaExiste(String numeroCuenta) {
        return cuentas.containsKey(numeroCuenta);
    }
    
    public double obtenerSaldo(String numeroCuenta) {
        return cuentas.getOrDefault(numeroCuenta, 0.0);
    }
    
    public String obtenerTitular(String numeroCuenta) {
        return titulares.getOrDefault(numeroCuenta, "Desconocido");
    }
    
    // Métodos de administración
    public void crearCuenta(String numeroCuenta, String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        cuentas.put(numeroCuenta, saldoInicial);
        titulares.put(numeroCuenta, titular);
    }
    
    public Map<String, Double> obtenerEstadoCuentas() {
        return new HashMap<>(cuentas); // Devolver copia para evitar modificación externa
    }
    
    /**
     * Clase para representar el resultado de una transferencia
     */
    public static class ResultadoTransferencia {
        private final boolean exitosa;
        private final String mensaje;
        
        public ResultadoTransferencia(boolean exitosa, String mensaje) {
            this.exitosa = exitosa;
            this.mensaje = mensaje;
        }
        
        public boolean isExitosa() { return exitosa; }
        public String getMensaje() { return mensaje; }
        
        @Override
        public String toString() {
            return (exitosa ? "✓ " : "✗ ") + mensaje;
        }
    }
}

// Clase principal demostrativa
public class Step1Procedural {
    public static void main(String[] args) {
        SistemaBancario banco = new SistemaBancario();
        
        // Probar diferentes escenarios
        System.out.println("=== SISTEMA BANCARIO MEJORADO ===");
        
        // Transferencia exitosa
        SistemaBancario.ResultadoTransferencia resultado1 = banco.transferir("A", "B", 200);
        System.out.println(resultado1);
        
        // Saldo insuficiente
        SistemaBancario.ResultadoTransferencia resultado2 = banco.transferir("A", "B", 5000);
        System.out.println(resultado2);
        
        // Cuenta no existe
        SistemaBancario.ResultadoTransferencia resultado3 = banco.transferir("A", "Z", 100);
        System.out.println(resultado3);
        
        // Mostrar estado final
        System.out.println("\n--- ESTADO FINAL DE CUENTAS ---");
        banco.obtenerEstadoCuentas().forEach((cuenta, saldo) -> {
            System.out.printf("Cuenta %s (%s): %.2f%n", 
                cuenta, banco.obtenerTitular(cuenta), saldo);
        });
    }
}

/*
Pruebas finales REFACTORED
*/
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaBancarioTest {
    
    private SistemaBancario banco;
    
    @BeforeEach
    public void setUp() {
        banco = new SistemaBancario();
    }
    
    @Test
    public void testTransferenciaExitosa() {
        // When
        SistemaBancario.ResultadoTransferencia resultado = banco.transferir("A", "B", 200);
        
        // Then
        assertTrue(resultado.isExitosa());
        assertEquals(800, banco.obtenerSaldo("A"), 0.001);
        assertEquals(700, banco.obtenerSaldo("B"), 0.001);
    }
    
    @Test
    public void testTransferenciaConSaldoInsuficiente() {
        // When
        SistemaBancario.ResultadoTransferencia resultado = banco.transferir("A", "B", 5000);
        
        // Then
        assertFalse(resultado.isExitosa());
        assertTrue(resultado.getMensaje().contains("Saldo insuficiente"));
        // Los saldos no deben cambiar
        assertEquals(1000, banco.obtenerSaldo("A"), 0.001);
        assertEquals(500, banco.obtenerSaldo("B"), 0.001);
    }
    
    @Test
    public void testTransferenciaMontoInvalido() {
        // When
        SistemaBancario.ResultadoTransferencia resultado = banco.transferir("A", "B", -50);
        
        // Then
        assertFalse(resultado.isExitosa());
        assertTrue(resultado.getMensaje().contains("monto debe ser positivo"));
    }
    
    @Test
    public void testCrearNuevaCuenta() {
        // When
        banco.crearCuenta("D", "David Torres", 1500);
        
        // Then
        assertTrue(banco.cuentaExiste("D"));
        assertEquals(1500, banco.obtenerSaldo("D"), 0.001);
        assertEquals("David Torres", banco.obtenerTitular("D"));
    }
}

