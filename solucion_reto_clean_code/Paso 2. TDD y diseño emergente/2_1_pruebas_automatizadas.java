/*
Pruebas Automatizadas para DiscountCalculator
*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCalculatorTest {

    // =============================================
    // PRUEBAS PARA CLIENTE PREFERENTE (10% descuento)
    // =============================================

    @Test
    @DisplayName("Debería aplicar 10% de descuento para cliente preferente")
    void testClientePreferente_DescuentoAplicado() {
        // Given
        double precioOriginal = 100.0;
        String tipoCliente = "preferente";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(90.0, precioConDescuento, 0.001, 
            "El descuento del 10% no se aplicó correctamente");
    }

    @Test
    @DisplayName("Debería aplicar descuento preferente a precio cero")
    void testClientePreferente_PrecioCero() {
        // Given
        double precioOriginal = 0.0;
        String tipoCliente = "preferente";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(0.0, precioConDescuento, 0.001,
            "El precio cero con descuento debería seguir siendo cero");
    }

    // =============================================
    // PRUEBAS PARA CLIENTE VIP (20% descuento)
    // =============================================

    @Test
    @DisplayName("Debería aplicar 20% de descuento para cliente VIP")
    void testClienteVip_DescuentoAplicado() {
        // Given
        double precioOriginal = 100.0;
        String tipoCliente = "vip";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(80.0, precioConDescuento, 0.001,
            "El descuento del 20% no se aplicó correctamente");
    }

    @Test
    @DisplayName("Debería aplicar descuento VIP a precios decimales")
    void testClienteVip_PrecioDecimal() {
        // Given
        double precioOriginal = 49.99;
        String tipoCliente = "vip";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(39.992, precioConDescuento, 0.001,
            "El descuento del 20% no se aplicó correctamente a precios decimales");
    }

    // =============================================
    // PRUEBAS PARA CLIENTE REGULAR (sin descuento)
    // =============================================

    @Test
    @DisplayName("No debería aplicar descuento para cliente regular")
    void testClienteRegular_SinDescuento() {
        // Given
        double precioOriginal = 100.0;
        String tipoCliente = "regular";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(100.0, precioConDescuento, 0.001,
            "No debería haber descuento para cliente regular");
    }

    @Test
    @DisplayName("Debería retornar mismo precio para tipo de cliente vacío")
    void testClienteVacio_SinDescuento() {
        // Given
        double precioOriginal = 100.0;
        String tipoCliente = "";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(100.0, precioConDescuento, 0.001,
            "No debería haber descuento para tipo de cliente vacío");
    }

    @Test
    @DisplayName("Debería retornar mismo precio para tipo de cliente null")
    void testClienteNull_SinDescuento() {
        // Given
        double precioOriginal = 100.0;
        String tipoCliente = null;

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(100.0, precioConDescuento, 0.001,
            "No debería haber descuento para tipo de cliente null");
    }

    // =============================================
    // PRUEBAS PARAMETRIZADAS (Múltiples casos)
    // =============================================

    @ParameterizedTest
    @DisplayName("Pruebas parametrizadas para diferentes tipos de cliente")
    @CsvSource({
        "100.0, preferente, 90.0",
        "100.0, vip, 80.0", 
        "100.0, regular, 100.0",
        "100.0, '', 100.0",
        "50.0, preferente, 45.0",
        "50.0, vip, 40.0",
        "0.0, preferente, 0.0",
        "0.0, vip, 0.0"
    })
    void testCalculoDescuento_Parametrizado(double precioOriginal, String tipoCliente, double expected) {
        // When
        double resultado = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(expected, resultado, 0.001,
            String.format("Precio: %.2f, Cliente: %s", precioOriginal, tipoCliente));
    }

    @ParameterizedTest
    @DisplayName("Debería manejar case-insensitive para tipos de cliente")
    @ValueSource(strings = {"PREFERENTE", "Preferente", "VIP", "Vip", "REGULAR"})
    void testCaseInsensitive_TiposCliente(String tipoCliente) {
        // Given
        double precioOriginal = 100.0;

        // When
        double resultado = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then - Todos deberían caer en el caso default (sin descuento)
        assertEquals(100.0, resultado, 0.001,
            "Los tipos de cliente deberían ser case-sensitive");
    }

    // =============================================
    // PRUEBAS DE VALORES LIMITE
    // =============================================

    @Test
    @DisplayName("Debería manejar precios negativos correctamente")
    void testPrecioNegativo() {
        // Given
        double precioOriginal = -50.0;
        String tipoCliente = "preferente";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then
        assertEquals(-45.0, precioConDescuento, 0.001,
            "El descuento debería aplicarse también a precios negativos");
    }

    @Test
    @DisplayName("Debería manejar precios muy grandes")
    void testPrecioMuyGrande() {
        // Given
        double precioOriginal = Double.MAX_VALUE / 2;
        String tipoCliente = "vip";

        // When
        double precioConDescuento = DiscountCalculator.calcularDescuento(precioOriginal, tipoCliente);

        // Then - No debería haber overflow
        assertEquals(precioOriginal * 0.8, precioConDescuento, 0.001,
            "Debería manejar precios grandes sin overflow");
    }

    // =============================================
    // PRUEBAS DE PROPIEDADES (Property-based testing)
    // =============================================

    @Test
    @DisplayName("El precio con descuento nunca debería ser mayor al precio original")
    void testPropiedad_PrecioNuncaMayor() {
        // Given - Probamos con varios precios
        double[] precios = {0.0, 10.0, 100.0, 1000.0, -50.0};
        String[] tiposCliente = {"preferente", "vip", "regular", "", null};

        for (double precio : precios) {
            for (String tipo : tiposCliente) {
                // When
                double precioConDescuento = DiscountCalculator.calcularDescuento(precio, tipo);

                // Then - Propiedad: precio con descuento <= precio original
                if (precio >= 0) {
                    assertTrue(precioConDescuento <= precio,
                        String.format("Precio con descuento (%.2f) no debería ser mayor que original (%.2f) para cliente %s",
                            precioConDescuento, precio, tipo));
                }
            }
        }
    }

    @Test
    @DisplayName("El descuento VIP siempre debería ser mayor que el preferente")
    void testPropiedad_DescuentoVipMayor() {
        // Given
        double precio = 100.0;

        // When
        double descuentoPreferente = DiscountCalculator.calcularDescuento(precio, "preferente");
        double descuentoVip = DiscountCalculator.calcularDescuento(precio, "vip");

        // Then
        assertTrue(descuentoVip < descuentoPreferente,
            "El descuento VIP debería ser mayor que el preferente");
    }
}