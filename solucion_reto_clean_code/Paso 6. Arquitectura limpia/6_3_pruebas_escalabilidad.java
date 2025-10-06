/*
Pruebas Unitarias con Inyección de Dependencias y Mocks
Caso de Uso Elegido: "Agregar Producto al Carrito"

Diseño del Caso de Uso con Dependencias Inyectadas
1. Interfaz del Repositorio (Abstracción)
*/
package domain.repositories;

import domain.Carrito;
import java.util.Optional;

public interface CarritoRepository {
    Optional<Carrito> buscarPorCliente(String idCliente);
    void guardar(Carrito carrito);
}

public interface ProductoRepository {
    Optional<Producto> buscarPorId(String id);
}

//2. Caso de Uso con Dependencias Inyectadas
package application.usecases;

import domain.Carrito;
import domain.Producto;
import domain.repositories.CarritoRepository;
import domain.repositories.ProductoRepository;
import domain.exceptions.ProductoNoEncontradoException;
import domain.exceptions.StockInsuficienteException;

public class AgregarProductoAlCarritoUseCase {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    
    public AgregarProductoAlCarritoUseCase(
            CarritoRepository carritoRepository,
            ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }
    
    public void ejecutar(String idCliente, String idProducto, int cantidad) {
        // Validar cantidad
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        
        // Buscar producto
        Producto producto = productoRepository.buscarPorId(idProducto)
            .orElseThrow(() -> new ProductoNoEncontradoException(idProducto));
        
        // Validar stock
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente para: " + producto.getNombre() + 
                ". Disponible: " + producto.getStock() + ", Solicitado: " + cantidad
            );
        }
        
        // Buscar o crear carrito
        Carrito carrito = carritoRepository.buscarPorCliente(idCliente)
            .orElse(new Carrito(idCliente));
        
        // Agregar item al carrito
        carrito.agregarItem(producto, cantidad);
        
        // Guardar cambios
        carritoRepository.guardar(carrito);
    }
}

//3. Entidades Enriquecidas con Validaciones
package domain;

public class Carrito {
    private String idCliente;
    private List<ItemCarrito> items = new ArrayList<>();
    
    public void agregarItem(Producto producto, int cantidad) {
        // Validación adicional en la entidad
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        items.add(new ItemCarrito(producto, cantidad));
    }
    
    // Método para testing
    public int getCantidadItems() {
        return items.size();
    }
    
    public Optional<ItemCarrito> buscarItemPorProducto(String productoId) {
        return items.stream()
            .filter(item -> item.getProducto().getId().equals(productoId))
            .findFirst();
    }
}

/*
 Pruebas Unitarias con Mocks
1. Configuración de Pruebas con Mockito
*/
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgregarProductoAlCarritoUseCaseTest {
    
    @Mock
    private CarritoRepository carritoRepository;
    
    @Mock
    private ProductoRepository productoRepository;
    
    private AgregarProductoAlCarritoUseCase useCase;
    private final String CLIENTE_ID = "cliente-123";
    private final String PRODUCTO_ID = "prod-456";
    
    @BeforeEach
    void setUp() {
        useCase = new AgregarProductoAlCarritoUseCase(carritoRepository, productoRepository);
    }
}

//2. Prueba: Agregar Producto a Carrito Existente
@Test
@DisplayName("Debería agregar producto a carrito existente exitosamente")
void testAgregarProducto_CarritoExistente() {
    // Given - Configurar mocks
    Producto producto = new Producto(PRODUCTO_ID, "Laptop", 1500.00, 10);
    Carrito carritoExistente = new Carrito(CLIENTE_ID);
    
    when(productoRepository.buscarPorId(PRODUCTO_ID))
        .thenReturn(Optional.of(producto));
    when(carritoRepository.buscarPorCliente(CLIENTE_ID))
        .thenReturn(Optional.of(carritoExistente));
    
    // When - Ejecutar caso de uso
    useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, 2);
    
    // Then - Verificar comportamiento
    verify(productoRepository).buscarPorId(PRODUCTO_ID);
    verify(carritoRepository).buscarPorCliente(CLIENTE_ID);
    verify(carritoRepository).guardar(carritoExistente);
    
    // Verificar que el item se agregó al carrito
    assertEquals(1, carritoExistente.getCantidadItems());
    assertTrue(carritoExistente.buscarItemPorProducto(PRODUCTO_ID).isPresent());
}

//3. Prueba: Crear Nuevo Carrito para Cliente
@Test
@DisplayName("Debería crear nuevo carrito cuando no existe")
void testAgregarProducto_CarritoNuevo() {
    // Given - Cliente sin carrito existente
    Producto producto = new Producto(PRODUCTO_ID, "Mouse", 25.00, 50);
    
    when(productoRepository.buscarPorId(PRODUCTO_ID))
        .thenReturn(Optional.of(producto));
    when(carritoRepository.buscarPorCliente(CLIENTE_ID))
        .thenReturn(Optional.empty());
    
    // When
    useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, 1);
    
    // Then - Verificar que se creó y guardó un nuevo carrito
    verify(carritoRepository).guardar(argThat(carrito -> 
        carrito.getCantidadItems() == 1 && 
        carrito.buscarItemPorProducto(PRODUCTO_ID).isPresent()
    ));
}

//4. Prueba: Producto No Encontrado
@Test
@DisplayName("Debería lanzar excepción cuando producto no existe")
void testAgregarProducto_ProductoNoEncontrado() {
    // Given - Producto no existe
    when(productoRepository.buscarPorId(PRODUCTO_ID))
        .thenReturn(Optional.empty());
    
    // When & Then - Verificar excepción
    ProductoNoEncontradoException exception = assertThrows(
        ProductoNoEncontradoException.class,
        () -> useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, 1)
    );
    
    assertEquals(PRODUCTO_ID, exception.getProductoId());
    verify(carritoRepository, never()).guardar(any());
}

//5. Prueba: Stock Insuficiente
@Test
@DisplayName("Debería lanzar excepción cuando stock es insuficiente")
void testAgregarProducto_StockInsuficiente() {
    // Given - Producto con stock insuficiente
    Producto producto = new Producto(PRODUCTO_ID, "Teclado", 75.00, 2);
    
    when(productoRepository.buscarPorId(PRODUCTO_ID))
        .thenReturn(Optional.of(producto));
    
    // When & Then - Verificar excepción por stock insuficiente
    StockInsuficienteException exception = assertThrows(
        StockInsuficienteException.class,
        () -> useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, 5) // Solicitar 5 cuando hay 2
    );
    
    assertTrue(exception.getMessage().contains("Stock insuficiente"));
    verify(carritoRepository, never()).guardar(any());
}

//6. Prueba: Cantidad Inválida
@Test
@DisplayName("Debería lanzar excepción cuando cantidad es inválida")
void testAgregarProducto_CantidadInvalida() {
    // Given - Cantidad cero o negativa
    int cantidadInvalida = 0;
    
    // When & Then - Verificar excepción por cantidad inválida
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, cantidadInvalida)
    );
    
    assertEquals("La cantidad debe ser positiva", exception.getMessage());
    verify(productoRepository, never()).buscarPorId(any());
    verify(carritoRepository, never()).guardar(any());
}

//7. Prueba: Comportamiento de Repositorio
@Test
@DisplayName("Debería manejar errores del repositorio correctamente")
void testAgregarProducto_ErrorRepositorio() {
    // Given - Repositorio lanza excepción
    Producto producto = new Producto(PRODUCTO_ID, "Monitor", 300.00, 5);
    
    when(productoRepository.buscarPorId(PRODUCTO_ID))
        .thenReturn(Optional.of(producto));
    when(carritoRepository.buscarPorCliente(CLIENTE_ID))
        .thenReturn(Optional.of(new Carrito(CLIENTE_ID)));
    doThrow(new RuntimeException("Error de base de datos"))
        .when(carritoRepository).guardar(any(Carrito.class));
    
    // When & Then - Verificar que la excepción se propaga
    assertThrows(RuntimeException.class,
        () -> useCase.ejecutar(CLIENTE_ID, PRODUCTO_ID, 1));
}

/*
Pruebas de Integración con Test Containers
Prueba de Integración con Base de Datos Real
*/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AgregarProductoAlCarritoIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:13");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQL::getUsername);
        registry.add("spring.datasource.password", postgreSQL::getPassword);
    }
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private CarritoRepositoryJpa carritoRepository;
    
    @Autowired
    private ProductoRepositoryJpa productoRepository;
    
    @Test
    @DisplayName("Integración: Debería persistir carrito con items en BD")
    void testIntegracion_PersistenciaCarrito() {
        // Given - Datos en base de datos real
        ProductoEntity productoEntity = new ProductoEntity("prod-999", "Tablet", 499.99, 20);
        entityManager.persist(productoEntity);
        
        AgregarProductoAlCarritoUseCase useCase = 
            new AgregarProductoAlCarritoUseCase(carritoRepository, productoRepository);
        
        // When
        useCase.ejecutar("cliente-test", "prod-999", 1);
        
        // Then - Verificar en base de datos real
        Carrito carritoGuardado = carritoRepository.buscarPorCliente("cliente-test")
            .orElseThrow();
        
        assertEquals(1, carritoGuardado.getCantidadItems());
    }
}

