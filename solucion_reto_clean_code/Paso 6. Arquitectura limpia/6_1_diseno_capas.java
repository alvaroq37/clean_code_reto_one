/*
Estructura de Capas

src/
├── domain/                          # Capa de Dominio
│   ├── entities/                    # Entidades de Negocio
│   ├── valueobjects/               # Objetos de Valor
│   ├── repositories/               # Interfaces de Repositorios
│   └── exceptions/                 # Excepciones del Dominio
├── application/                    # Capa de Aplicación
│   ├── usecases/                   # Casos de Uso
│   ├── services/                   # Servicios de Aplicación
│   └── dtos/                       # Objetos de Transferencia
├── infrastructure/                 # Capa de Infraestructura
│   ├── persistence/               # Implementaciones de Persistencia
│   ├── external/                  # Servicios Externos
│   └── config/                    # Configuración
└── interfaces/                     # Capa de Interfaces
    ├── web/                       # Controladores REST
    ├── cli/                       # Interfaces de Línea de Comandos
    └── events/                    # Manejadores de Eventos
*/

//Capa de Dominio (Entities)
// domain/entities/
public class Carrito {
    private String idCliente;
    private List<ItemCarrito> items = new ArrayList<>();
    
    // MÉTODOS ENRIQUECIDOS CON LÓGICA DE NEGOCIO
    public void agregarItem(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (!producto.tieneStockSuficiente(cantidad)) {
            throw new StockInsuficienteException("Stock insuficiente para: " + producto.getNombre());
        }
        items.add(new ItemCarrito(producto, cantidad));
    }
    
    public double calcularTotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
    }
    
    public Pedido crearPedido() {
        if (items.isEmpty()) {
            throw new CarritoVacioException("No se puede crear pedido con carrito vacío");
        }
        return new Pedido(UUID.randomUUID().toString(), this.calcularTotal());
    }
}

// domain/entities/
public class Producto {
    private String id;
    private String nombre;
    private double precio;
    private int stock;
    
    // LÓGICA DE NEGOCIO ENRIQUECIDA
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }
    
    public void reservarStock(int cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new StockInsuficienteException("No hay suficiente stock para reservar");
        }
        this.stock -= cantidad;
    }
    
    public void liberarStock(int cantidad) {
        this.stock += cantidad;
    }
}

// domain/entities/
public class Pedido {
    private String id;
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    private double total;
    private LocalDateTime fechaCreacion;
    
    // ENUM PARA ESTADOS VÁLIDOS
    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    }
    
    public void confirmar() {
        if (this.estado != EstadoPedido.PENDIENTE) {
            throw new CambioEstadoInvalidoException("Solo pedidos pendientes pueden confirmarse");
        }
        this.estado = EstadoPedido.CONFIRMADO;
    }
    
    public void cancelar() {
        if (this.estado == EstadoPedido.ENVIADO || this.estado == EstadoPedido.ENTREGADO) {
            throw new CambioEstadoInvalidoException("No se puede cancelar un pedido enviado o entregado");
        }
        this.estado = EstadoPedido.CANCELADO;
    }
}

//Interfaces de Repositorio (en Domain)
// domain/repositories/
public interface CarritoRepository {
    Optional<Carrito> buscarPorCliente(String idCliente);
    void guardar(Carrito carrito);
    void eliminar(String idCliente);
}

// domain/repositories/
public interface ProductoRepository {
    Optional<Producto> buscarPorId(String id);
    List<Producto> buscarTodos();
    void guardar(Producto producto);
}

// domain/repositories/
public interface PedidoRepository {
    Optional<Pedido> buscarPorId(String id);
    void guardar(Pedido pedido);
}

// domain/repositories/
public interface PagoRepository {
    void guardar(Pago pago);
    Optional<Pago> buscarPorPedido(String idPedido);
}

//CAPA APLICACION (Use Cases)
// application/usecases/
public class AgregarProductoAlCarritoUseCase {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    
    public AgregarProductoAlCarritoUseCase(CarritoRepository carritoRepository, 
                                         ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }
    
    public void ejecutar(String idCliente, String idProducto, int cantidad) {
        // 1. Buscar producto
        Producto producto = productoRepository.buscarPorId(idProducto)
            .orElseThrow(() -> new ProductoNoEncontradoException(idProducto));
        
        // 2. Buscar o crear carrito
        Carrito carrito = carritoRepository.buscarPorCliente(idCliente)
            .orElse(new Carrito(idCliente));
        
        // 3. Agregar item al carrito
        carrito.agregarItem(producto, cantidad);
        
        // 4. Guardar cambios
        carritoRepository.guardar(carrito);
    }
}

// application/usecases/
public class CrearPedidoUseCase {
    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    
    public CrearPedidoUseCase(CarritoRepository carritoRepository,
                            PedidoRepository pedidoRepository,
                            ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }
    
    public Pedido ejecutar(String idCliente) {
        // 1. Buscar carrito
        Carrito carrito = carritoRepository.buscarPorCliente(idCliente)
            .orElseThrow(() -> new CarritoNoEncontradoException(idCliente));
        
        // 2. Crear pedido desde carrito
        Pedido pedido = carrito.crearPedido();
        
        // 3. Reservar stock de productos
        carrito.getItems().forEach(item -> {
            Producto producto = item.getProducto();
            producto.reservarStock(item.getCantidad());
            productoRepository.guardar(producto);
        });
        
        // 4. Guardar pedido
        pedidoRepository.guardar(pedido);
        
        // 5. Limpiar carrito
        carritoRepository.eliminar(idCliente);
        
        return pedido;
    }
}

// application/usecases/
public class ProcesarPagoUseCase {
    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ServicioPagosExterno servicioPagos;
    
    public ProcesarPagoUseCase(PedidoRepository pedidoRepository,
                             PagoRepository pagoRepository,
                             ServicioPagosExterno servicioPagos) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.servicioPagos = servicioPagos;
    }
    
    public ResultadoPago ejecutar(String idPedido, String medioPago, double monto) {
        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(idPedido)
            .orElseThrow(() -> new PedidoNoEncontradoException(idPedido));
        
        // 2. Crear pago
        Pago pago = new Pago(idPedido, monto, medioPago);
        
        // 3. Procesar pago externamente
        try {
            pago.procesar(); // Lógica delegada al servicio externo
            pagoRepository.guardar(pago);
            
            // 4. Actualizar estado del pedido
            pedido.confirmar();
            pedidoRepository.guardar(pedido);
            
            return ResultadoPago.exitoso(pago);
        } catch (Exception e) {
            return ResultadoPago.fallido("Error procesando pago: " + e.getMessage());
        }
    }
}

//DTOs DE APLICACION
// application/dtos/
public class CarritoDTO {
    private String idCliente;
    private List<ItemCarritoDTO> items;
    private double total;
    
    // Getters, setters, constructor
}

// application/dtos/
public class ResultadoPago {
    private final boolean exitoso;
    private final String mensaje;
    private final String idTransaccion;
    
    // Factory methods
    public static ResultadoPago exitoso(Pago pago) {
        return new ResultadoPago(true, "Pago procesado exitosamente", pago.getId());
    }
    
    public static ResultadoPago fallido(String mensajeError) {
        return new ResultadoPago(false, mensajeError, null);
    }
}

//CAPA DE INTERFACES (Adapters)
// interfaces/web/
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final AgregarProductoAlCarritoUseCase agregarProductoUseCase;
    private final CrearPedidoUseCase crearPedidoUseCase;
    
    public CarritoController(AgregarProductoAlCarritoUseCase agregarProductoUseCase,
                           CrearPedidoUseCase crearPedidoUseCase) {
        this.agregarProductoUseCase = agregarProductoUseCase;
        this.crearPedidoUseCase = crearPedidoUseCase;
    }
    
    @PostMapping("/{idCliente}/items")
    public ResponseEntity<?> agregarItem(@PathVariable String idCliente,
                                       @RequestBody AgregarItemRequest request) {
        try {
            agregarProductoUseCase.ejecutar(idCliente, request.getProductoId(), request.getCantidad());
            return ResponseEntity.ok().build();
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (StockInsuficienteException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("STOCK_INSUFICIENTE", e.getMessage()));
        }
    }
    
    @PostMapping("/{idCliente}/checkout")
    public ResponseEntity<PedidoResponse> checkout(@PathVariable String idCliente) {
        try {
            Pedido pedido = crearPedidoUseCase.ejecutar(idCliente);
            return ResponseEntity.ok(PedidoResponse.from(pedido));
        } catch (CarritoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

// interfaces/web/
@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final ProcesarPagoUseCase procesarPagoUseCase;
    
    public PagoController(ProcesarPagoUseCase procesarPagoUseCase) {
        this.procesarPagoUseCase = procesarPagoUseCase;
    }
    
    @PostMapping
    public ResponseEntity<ResultadoPagoResponse> procesarPago(@RequestBody ProcesarPagoRequest request) {
        ResultadoPago resultado = procesarPagoUseCase.ejecutar(
            request.getPedidoId(), 
            request.getMedioPago(), 
            request.getMonto()
        );
        
        return ResponseEntity.ok(ResultadoPagoResponse.from(resultado));
    }
}

//CAPA DE INFRAESTRUCTURA (FRAMEWORKS)
// infrastructure/persistence/
@Repository
public class CarritoRepositoryJpa implements CarritoRepository {
    private final CarritoJpaRepository jpaRepository;
    
    public CarritoRepositoryJpa(CarritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<Carrito> buscarPorCliente(String idCliente) {
        return jpaRepository.findByClienteId(idCliente)
                .map(this::toDomain);
    }
    
    @Override
    public void guardar(Carrito carrito) {
        CarritoEntity entity = toEntity(carrito);
        jpaRepository.save(entity);
    }
    
    private Carrito toDomain(CarritoEntity entity) {
        // Mapeo de Entity a Domain
        return new Carrito(entity.getClienteId());
    }
}

// infrastructure/external/
@Component
public class ServicioPagosStripe implements ServicioPagosExterno {
    private final StripeClient stripeClient;
    
    public ServicioPagosStripe(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }
    
    @Override
    public void procesarPago(Pago pago) {
        // Integración con Stripe
        PaymentIntent intent = stripeClient.createPaymentIntent(
            pago.getMonto(),
            pago.getMedioPago()
        );
        
        if (!intent.getStatus().equals("succeeded")) {
            throw new PagoRechazadoException("Pago rechazado por el procesador");
        }
    }
}

// infrastructure/config/
@Configuration
public class UseCaseConfig {
    
    @Bean
    public AgregarProductoAlCarritoUseCase agregarProductoAlCarritoUseCase(
            CarritoRepository carritoRepository,
            ProductoRepository productoRepository) {
        return new AgregarProductoAlCarritoUseCase(carritoRepository, productoRepository);
    }
    
    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(
            CarritoRepository carritoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        return new CrearPedidoUseCase(carritoRepository, pedidoRepository, productoRepository);
    }
}