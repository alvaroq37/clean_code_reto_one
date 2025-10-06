import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas básicas para el cálculo de descuentos.  Ejecuta estos tests con
 * un runner de JUnit 5 para verificar el comportamiento antes y después de
 * refactorizar la función.
 */
public class DiscountCalculatorTest {
    @Test
    public void testPreferente() {
        assertEquals(90.0, DiscountCalculator.calcularDescuento(100, "preferente"));
    }

    @Test
    public void testVip() {
        assertEquals(160.0, DiscountCalculator.calcularDescuento(200, "vip"));
    }

    @Test
    public void testOtros() {
        assertEquals(50.0, DiscountCalculator.calcularDescuento(50, "normal"));
    }
}